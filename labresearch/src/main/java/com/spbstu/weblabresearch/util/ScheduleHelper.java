package com.spbstu.weblabresearch.util;

import com.spbstu.weblabresearch.dbo.*;
import com.spbstu.weblabresearch.repos.BusyRepository;
import com.spbstu.weblabresearch.repos.ScheduleRepository;
import com.spbstu.weblabresearch.repos.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ScheduleHelper {
    private final UserRepository userRepo;
    private final ScheduleRepository scheduleRepo;
    private final BusyRepository busyRepo;

    public ScheduleHelper(UserRepository userRepo,
                          ScheduleRepository scheduleRepo,
                          BusyRepository busyRepo) {
        this.userRepo = userRepo;
        this.scheduleRepo = scheduleRepo;
        this.busyRepo = busyRepo;
    }

    public List<TimeSpan> getAvailableStartTimesForCollection(Request request) {
        int reqMin = getRequiredMinutesForCollection(request);
        List<AvailableAssistant> availableList = getAvailableStartTimes(request, reqMin);
        return getMergedTimeSpanList(availableList);
    }

    public List<AvailableAssistant> getAvailableStartTimesForResearch(Request request) {
        int reqMin = 0;
        return getAvailableStartTimes(request, reqMin);
    }

    private List<AvailableAssistant> getAvailableStartTimes(Request request, int reqMinutes) {
        Date reqDate = getOnlyDate(request.getArrivalTime());
        Weekday reqWeekday = getWeekday(reqDate);
        List<User> assistants = userRepo.findByRole(Role.ASSISTANT);
        List<AvailableAssistant> availableAssistants = new ArrayList<>();
        for (User assistant : assistants) {
            List<TimeSpan> schedule = getSchedule(assistant, reqWeekday);
            List<TimeSpan> busy = getBusyByDate(assistant, reqDate);
            List<TimeSpan> free = getFreeTime(schedule, busy);
            List<TimeSpan> startTimeList = getStartTime(free, reqMinutes);
            availableAssistants.add(new AvailableAssistant(assistant, startTimeList));
        }
        return availableAssistants;
    }

    private List<TimeSpan> getMergedTimeSpanList(List<AvailableAssistant> availableAssistants) {
        List<TimeSpan> result = new ArrayList<>();
        for (AvailableAssistant availableAssistant : availableAssistants) {
            List<TimeSpan> timeSpanList = availableAssistant.getAvailableTimeSpanList();
            result.addAll(timeSpanList);
        }
        return TimeSpan.mergeAll(result);
    }

    private static Date getOnlyDate(Date dateTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.clear();
        c.set(year, month, day);
        return c.getTime();
    }

    // Количество минут, требуемое на сбор биоматериала
    private int getRequiredMinutesForCollection(Request request) {
        return request.getAnalysisList()
                .stream()
                .mapToInt(Analysis::getCollectionMinutes)
                .sum();
    }

    private static Weekday getWeekday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekdayNumber = c.get(Calendar.DAY_OF_WEEK);
        return Weekday.getByNumber(weekdayNumber);
    }

    private List<TimeSpan> getSchedule(User assistant, Weekday weekday) {
        return scheduleRepo.findByAssistantAndWeekdayOrderByStartTimeAsc(assistant, weekday)
                .stream()
                .map(x -> new TimeSpan(x.getStartTime(), x.getEndTime()))
                .collect(Collectors.toList());
    }

    private List<TimeSpan> getBusyByDate(User assistant, Date date) {
        return busyRepo.findByAssistantAndTheDateOrderByStartTimeAsc(assistant, new java.sql.Date(date.getTime()))
                .stream()
                .map(x -> new TimeSpan(x.getStartTime(), x.getEndTime()))
                .collect(Collectors.toList());
    }

    private static List<TimeSpan> getFreeTime(List<TimeSpan> schedule, List<TimeSpan> busy) {
        return TimeSpan.subtractAll(schedule, busy);
    }

    // Получает диапазоны времён, которые доступны для времени начала выполнения
    // задачи, которая требует reqMin минут.
    private static List<TimeSpan> getStartTime(List<TimeSpan> freeTime, int reqMin) {
        List<TimeSpan> res = new ArrayList<>();
        for (TimeSpan freeTimeItem : freeTime) {
            TimeSpan reduced = freeTimeItem.reduceEnd(reqMin);
            if (reduced != null) {
                res.add(reduced);
            }
        }
        return res;
    }

    public User chooseRandomAssistant(Request request, Time time) {
        int reqMin = getRequiredMinutesForCollection(request);
        List<AvailableAssistant> availableList = getAvailableStartTimes(request, reqMin);
        List<User> suitableAssistants = getSuitableAssistants(availableList, time);
        if (suitableAssistants.size() <= 0) {
            return null;
        }
        int randIndex = ThreadLocalRandom.current().nextInt(suitableAssistants.size());
        return suitableAssistants.get(randIndex);
    }

    private List<User> getSuitableAssistants(List<AvailableAssistant> availableList, Time time) {
        List<User> res = new ArrayList<>();
        for (AvailableAssistant assistant : availableList) {
            if (assistantCanBeUsed(assistant, time)) {
                res.add(assistant.getAssistant());
            }
        }
        return res;
    }

    private boolean assistantCanBeUsed(AvailableAssistant assistant, Time time) {
        List<TimeSpan> timeSpanList = assistant.getAvailableTimeSpanList();
        for (TimeSpan ts : timeSpanList) {
            if (ts.contains(time)) {
                return true;
            }
        }
        return false;
    }

    // returns collection min count for this
    public static int setCollectionTimes(Request request, Form form, int minOffset) {
        Analysis analysis = form.getAnalysis();
        int collectionReqMin = analysis.getCollectionMinutes();
        Calendar c = Calendar.getInstance();
        c.setTime(request.getArrivalTime());
        c.add(Calendar.MINUTE, minOffset);
        var startTime = c.getTime();
        c.add(Calendar.MINUTE, collectionReqMin);
        var endTime = c.getTime();
        form.setCollectionTime(startTime, endTime);
        return collectionReqMin;
    }

    public void setResearchTimes(Form form) {
        Analysis analysis = form.getAnalysis();
        int researchReqMin = analysis.getResearchMinutes();
        Date researchStartTime = chooseResearchStartTime(form, researchReqMin);
        var startTime = researchStartTime;
        Calendar c = Calendar.getInstance();
        c.setTime(researchStartTime);
        c.add(Calendar.MINUTE, researchReqMin);
        var endTime = c.getTime();
        form.setResearchTime(startTime, endTime);
    }

    private Date chooseResearchStartTime(Form form, int researchReqMin) {
        Date collectionEnd = form.getCollectionEndTime();
        Date curDate = getOnlyDate(collectionEnd);
        Calendar c = Calendar.getInstance();
        User assistant = form.getAssistant();
        boolean isFirstDate = true;
        while (true) {
            List<TimeSpan> availableTimes = getAvailableStartTimes(curDate, assistant, researchReqMin);
            for (TimeSpan ts : availableTimes) {
                c.setTime(curDate);
                c.set(Calendar.HOUR_OF_DAY, ts.getStartTime().getHour());
                c.set(Calendar.MINUTE, ts.getStartTime().getMinute());
                Date tmp = c.getTime();
                if (ts.getDurationAsMinutes() < researchReqMin) {
                    continue;
                }
                if (isFirstDate) {
                    if (tmp.before(collectionEnd)) {
                        continue;
                    }
                }
                return tmp;
            }
            c.setTime(curDate);
            c.add(Calendar.DATE, 1);
            curDate = c.getTime();
            isFirstDate = false;
        }
    }

    private List<TimeSpan> getAvailableStartTimes(Date date, User assistant, int reqMinutes) {
        Weekday reqWeekday = getWeekday(date);
        List<TimeSpan> schedule = getSchedule(assistant, reqWeekday);
        List<TimeSpan> busy = getBusyByDate(assistant, date);
        List<TimeSpan> free = getFreeTime(schedule, busy);
        List<TimeSpan> startTimeList = getStartTime(free, reqMinutes);
        return startTimeList;
    }
}

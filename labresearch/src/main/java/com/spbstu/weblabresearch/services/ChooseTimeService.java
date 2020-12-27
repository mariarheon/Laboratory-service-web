package com.spbstu.weblabresearch.services;

import com.spbstu.weblabresearch.dbo.*;
import com.spbstu.weblabresearch.dto.response.PossibleTimesDto;
import com.spbstu.weblabresearch.repos.BusyRepository;
import com.spbstu.weblabresearch.repos.RequestRepository;
import com.spbstu.weblabresearch.repos.ScheduleRepository;
import com.spbstu.weblabresearch.repos.UserRepository;
import com.spbstu.weblabresearch.util.ScheduleHelper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChooseTimeService {
    @NonNull
    UserRepository userRepo;

    @NonNull
    ScheduleRepository scheduleRepo;

    @NonNull
    BusyRepository busyRepo;

    @NonNull
    RequestRepository requests;

    public PossibleTimesDto getPossibleTimes(final int requestId) {
        var opt = requests.findById(requestId);
        if (opt.isEmpty()) {
            return new PossibleTimesDto("Заявка не найдена");
        }
        var request = opt.get();
        try {
            return new PossibleTimesDto(getTimeForAppointment(request));
        } catch (Exception ex) {
            return new PossibleTimesDto(ex.getMessage());
        }
    }

    private ScheduleHelper createScheduleHelper() {
        return new ScheduleHelper(
                userRepo,
                scheduleRepo,
                busyRepo);
    }

    private List<TimeSpan> getTimeForAppointment(Request request) {
        return createScheduleHelper().getAvailableStartTimesForCollection(request);
    }

    public User chooseRandomAssistant(Request request, Time time) {
        return createScheduleHelper().chooseRandomAssistant(request, time);
    }

    public int setCollectionTimes(Request request, Form form, int minOffset) {
        return ScheduleHelper.setCollectionTimes(request, form, minOffset);
    }

    public void setResearchTimes(Form form) {
        createScheduleHelper().setResearchTimes(form);
    }
}

package com.spbstu.weblabresearch.controllers;

import com.spbstu.weblabresearch.dbo.*;
import com.spbstu.weblabresearch.dto.request.RequestArrivalTimeDto;
import com.spbstu.weblabresearch.dto.response.PossibleTimesDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
import com.spbstu.weblabresearch.exceptions.BadChoosedTimeException;
import com.spbstu.weblabresearch.repos.FormRepository;
import com.spbstu.weblabresearch.repos.RequestRepository;
import com.spbstu.weblabresearch.services.ChooseTimeService;
import com.spbstu.weblabresearch.services.FormService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RequestMapping("/choose_time")
public class ChooseTimeController {
    @NonNull
    ChooseTimeService chooseTimeService;

    @NonNull
    FormService formService;

    @NonNull
    RequestRepository requestRepo;

    @NonNull
    FormRepository formRepo;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get_possible_times")
    PossibleTimesDto getPossibleTimes(@RequestParam("request_id") final int requestId) {
        return chooseTimeService.getPossibleTimes(requestId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/set_arrival_time")
    SuccessDto setArrivalTime(@RequestBody RequestArrivalTimeDto requestArrivalTimeDto) {
        var possibleTimesDto = chooseTimeService.getPossibleTimes(requestArrivalTimeDto.getRequestId());
        var er = possibleTimesDto.getEr();
        if (er != null && !er.isEmpty()) {
            return new SuccessDto("Выбранное время уже занято. Попробуйте снова");
        }
        try {
            var request = requestRepo.findById(requestArrivalTimeDto.getRequestId());
            if (request.isEmpty()) {
                return new SuccessDto("Редактируемая заявка более не существует");
            }
            sendToRandomAssistant(request.get(), requestArrivalTimeDto.getTime());
            return new SuccessDto();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex.getMessage() == null || ex.getMessage().isEmpty()) {
                return new SuccessDto("Ошибка сервера");
            }
            return new SuccessDto(ex.getMessage());
        }
    }

    private void sendToRandomAssistant(Request request, Time time) throws BadChoosedTimeException {
        User assistant = chooseTimeService.chooseRandomAssistant(request, time);
        if (assistant == null) {
            throw new BadChoosedTimeException();
        }
        request.setArrivalTime(addTimeToDate(request.getArrivalTime(), time));
        request = requestRepo.save(request);
        addForms(request, assistant);
    }

    private java.util.Date addTimeToDate(java.util.Date date, Time time) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTime(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                time.getHour(), time.getMinute(), 0);
        return c.getTime();
    }

    private void addForms(Request request, User assistant) {
        var analysisList = request.getAnalysisList();
        int minOffset = 0;
        List<Form> forms = new ArrayList<>();
        for (var analysis : analysisList) {
            Form form = new Form();
            form.setRequest(request);
            form.setAnalysis(analysis);
            form.setAssistant(assistant);
            minOffset += chooseTimeService.setCollectionTimes(request, form, minOffset);
            formService.setEmptyFields(form);
            form = formRepo.save(form);
            forms.add(form);
        }
        for (Form form : forms) {
            chooseTimeService.setResearchTimes(form);
            formRepo.save(form);
        }
        request.setStatus(RequestStatus.IN_WORK);
        requestRepo.save(request);
    }
}

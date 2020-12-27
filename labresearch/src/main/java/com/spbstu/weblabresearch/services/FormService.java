package com.spbstu.weblabresearch.services;

import com.spbstu.weblabresearch.dbo.*;
import com.spbstu.weblabresearch.dto.common.RequestDto;
import com.spbstu.weblabresearch.dto.response.FormDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
import com.spbstu.weblabresearch.repos.FormFieldRepository;
import com.spbstu.weblabresearch.repos.FormRepository;
import com.spbstu.weblabresearch.repos.RequestRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FormService {
    @NonNull
    FormRepository formRepo;

    @NonNull
    FormFieldRepository fieldRepo;

    @NonNull
    RequestRepository requestRepo;

    public List<FormDto> findByAssistant(User assistant) {
        return formRepo
                .findByAssistantAndStatus(assistant, FormStatus.IN_WORK)
                .stream()
                .map(x -> new FormDto(x, false))
                .collect(Collectors.toList());
    }

    public FormDto findById(User assistant, final int formId) {
        return formRepo
                .findById(formId)
                .filter(x -> x.getAssistant().getId() == assistant.getId() && x.getStatus() == FormStatus.IN_WORK)
                .map(x -> new FormDto(x, true))
                .orElse(new FormDto("Бланк не найден"));
    }

    public FormDto findByIdForClient(User client, final int formId) {
        return formRepo
                .findById(formId)
                .filter(x -> x.getRequest().getClient().getId() == client.getId() && x.getStatus() == FormStatus.FINISHED)
                .map(x -> new FormDto(x, true))
                .orElse(new FormDto("Бланк не найден"));
    }

    public List<FormDto> findByRequestId(User client, final int requestId) {
        var requestOpt = requestRepo.findById(requestId)
                .filter(x -> x.getClient().getId() == client.getId());
        if (requestOpt.isEmpty()) {
            return new ArrayList<FormDto>();
        }
        return formRepo.findByRequest(requestOpt.get())
                .stream()
                .map(x -> new FormDto(x, false))
                .collect(Collectors.toList());
    }

    public SuccessDto editFields(final User assistant, final FormDto formDto) {
        var opt = formRepo
                .findById(formDto.getId())
                .filter(x -> x.getAssistant().getId() == assistant.getId() && x.getStatus() == FormStatus.IN_WORK);
        if (opt.isEmpty()) {
            return new SuccessDto("Бланк не найден");
        }
        var form = opt.get();
        for (var link : form.getFormFieldLinks()) {
            int fieldId = link.getField().getId();
            formDto.getFields()
                    .stream()
                    .filter(x -> x.getId() == fieldId)
                    .findFirst()
                    .ifPresent(fieldDto -> link.setValue(fieldDto.getValue()));
        }
        formRepo.save(form);
        return new SuccessDto();
    }

    public SuccessDto finish(final User assistant, final int formId) {
        var opt = formRepo
                .findById(formId)
                .filter(x -> x.getAssistant().getId() == assistant.getId() && x.getStatus() == FormStatus.IN_WORK);
        if (opt.isEmpty()) {
            return new SuccessDto("Бланк не найден");
        }
        var form = opt.get();
        form.setStatus(FormStatus.FINISHED);
        form = formRepo.save(form);
        var request = form.getRequest();
        var forms = formRepo.findByRequest(request);
        if (forms
                .stream()
                .allMatch(x -> x.getStatus() == FormStatus.FINISHED)) {
            request.setStatus(RequestStatus.FINISHED);
            requestRepo.save(request);
        }
        return new SuccessDto();
    }

    public void setEmptyFields(Form form) {
        var fields = fieldRepo.findByAnalysisOrderByOrderNumAsc(form.getAnalysis());
        var links = fields
                .stream()
                .map(x -> {
                    var link = new FormFieldLink();
                    link.setForm(form);
                    link.setField(x);
                    link.setValue("");
                    return link;
                })
                .collect(Collectors.toList());
        form.setFormFieldLinks(links);
    }
}

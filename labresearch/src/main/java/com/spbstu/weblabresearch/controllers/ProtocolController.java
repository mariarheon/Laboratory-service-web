package com.spbstu.weblabresearch.controllers;

import com.spbstu.weblabresearch.dbo.FormStatus;
import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dto.response.FormDto;
import com.spbstu.weblabresearch.dto.response.ProtocolDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
import com.spbstu.weblabresearch.repos.FormRepository;
import com.spbstu.weblabresearch.repos.ProtocolRepository;
import com.spbstu.weblabresearch.services.FormService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RequestMapping("/protocol")
public class ProtocolController {
    @NonNull
    FormRepository forms;

    @NonNull
    ProtocolRepository protocols;

    @PreAuthorize("hasAuthority('ASSISTANT')")
    @GetMapping("/get_by_form_id")
    ProtocolDto getByFormId(@AuthenticationPrincipal final User assistant,
                            @RequestParam("form_id") final int formId) {
        var formOpt = forms
                .findById(formId)
                .filter(x -> x.getAssistant().getId() == assistant.getId() && x.getStatus() == FormStatus.IN_WORK);
        if (formOpt.isEmpty()) {
            return new ProtocolDto("Бланк не найден");
        }
        var form = formOpt.get();
        var protocolOpt = protocols.findByAnalysis(form.getAnalysis());
        if (protocolOpt.isEmpty()) {
            return new ProtocolDto("Протокол не найден");
        }
        return new ProtocolDto(protocolOpt.get());
    }
}

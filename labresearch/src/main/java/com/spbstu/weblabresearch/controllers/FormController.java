package com.spbstu.weblabresearch.controllers;

import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dto.common.RequestDto;
import com.spbstu.weblabresearch.dto.response.FormDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
import com.spbstu.weblabresearch.services.FormService;
import com.spbstu.weblabresearch.services.RequestService;
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
@RequestMapping("/form")
public class FormController {
    @NonNull
    FormService forms;

    @PreAuthorize("hasAuthority('ASSISTANT')")
    @GetMapping("/get_mine")
    List<FormDto> getMine(@AuthenticationPrincipal final User user) {
        return forms.findByAssistant(user);
    }

    @PreAuthorize("hasAuthority('ASSISTANT')")
    @GetMapping("/get_by_id")
    FormDto getById(@AuthenticationPrincipal final User user,
                    @RequestParam("id") final int formId) {
        return forms.findById(user, formId);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/get_by_id_for_client")
    FormDto getByIdForClient(@AuthenticationPrincipal final User client,
                    @RequestParam("id") final int formId) {
        return forms.findByIdForClient(client, formId);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/find_by_request_id")
    List<FormDto> findByRequestId(@AuthenticationPrincipal final User client,
                    @RequestParam("request_id") final int requestId) {
        return forms.findByRequestId(client, requestId);
    }

    @PreAuthorize("hasAuthority('ASSISTANT')")
    @PostMapping("/edit_fields")
    SuccessDto editFields(@AuthenticationPrincipal final User user,
                          @RequestBody final FormDto formDto) {
        return forms.editFields(user, formDto);
    }

    @PreAuthorize("hasAuthority('ASSISTANT')")
    @GetMapping("/finish")
    SuccessDto finish(@AuthenticationPrincipal final User user,
                      @RequestParam("form_id") final int formId) {
        return forms.finish(user, formId);
    }
}

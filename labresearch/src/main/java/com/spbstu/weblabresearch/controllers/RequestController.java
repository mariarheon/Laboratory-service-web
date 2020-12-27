package com.spbstu.weblabresearch.controllers;

import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dto.common.RequestDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
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
@RequestMapping("/request")
public class RequestController {
    @NonNull
    RequestService requests;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get_all")
    List<RequestDto> getAll() {
        return requests.findAll();
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/get_mine")
    List<RequestDto> getMine(@AuthenticationPrincipal final User user) {
        return requests.findByClient(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get")
    RequestDto get(
            @RequestParam("id") final int id) {
        return requests.findById(id).orElse(new RequestDto("Не найден"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit")
    public SuccessDto edit(@RequestBody final RequestDto request) {
        return requests.edit(request);
    }


    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/add")
    public SuccessDto add(@AuthenticationPrincipal final User client,
                          @RequestBody final RequestDto request) {
        return requests.add(client, request);
    }
}

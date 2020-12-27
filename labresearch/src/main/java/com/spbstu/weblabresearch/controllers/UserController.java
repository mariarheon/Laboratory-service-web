package com.spbstu.weblabresearch.controllers;

/**
 *
 */
import com.spbstu.weblabresearch.services.UserAuthenticationService;
import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dto.response.UserDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class UserController {
    @NonNull
    UserAuthenticationService authentication;

    @GetMapping("/current")
    UserDto getCurrent(@AuthenticationPrincipal final User user) {
        return new UserDto(user);
    }

    @GetMapping("/logout")
    boolean logout(@AuthenticationPrincipal final User user) {
        authentication.logout(user);
        return true;
    }
}

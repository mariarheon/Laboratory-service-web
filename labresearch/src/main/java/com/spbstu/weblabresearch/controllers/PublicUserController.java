package com.spbstu.weblabresearch.controllers;

/**
 *
 */
import com.spbstu.weblabresearch.dto.request.LoginDto;
import com.spbstu.weblabresearch.dto.request.RegisterDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
import com.spbstu.weblabresearch.dto.response.TokenDto;
import com.spbstu.weblabresearch.services.UserAuthenticationService;
import com.spbstu.weblabresearch.repos.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/public/user")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class PublicUserController {
    @NonNull
    UserAuthenticationService authentication;
    @NonNull
    UserRepository userRepo;

    @PostMapping("/register")
    SuccessDto register(@RequestBody final RegisterDto registerDto) {
        return authentication.register(registerDto);
    }

    @PostMapping("/login")
    TokenDto login(@RequestBody final LoginDto loginDto) {
        return authentication
                .login(loginDto.getLogin(), loginDto.getPassword())
                .map(x -> new TokenDto(x, null))
                .orElse(new TokenDto("Неверный логин и/или пароль"));
    }
}

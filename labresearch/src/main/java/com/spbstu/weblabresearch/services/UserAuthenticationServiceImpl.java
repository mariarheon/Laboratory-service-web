package com.spbstu.weblabresearch.services;

/**
 *
 */
import com.spbstu.weblabresearch.dbo.Role;
import com.spbstu.weblabresearch.dbo.Sex;
import com.spbstu.weblabresearch.dbo.Token;
import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dto.request.RegisterDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
import com.spbstu.weblabresearch.repos.TokenRepository;
import com.spbstu.weblabresearch.repos.UserRepository;
import com.spbstu.weblabresearch.util.StringTypeVerifier;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class UserAuthenticationServiceImpl implements UserAuthenticationService {
    @NonNull
    private UserRepository userRepo;

    @NonNull
    private TokenRepository tokenRepo;

    @Override
    public SuccessDto register(final RegisterDto registerDto) {
        String errorMsgKey = "errorMsg";
        try {
            String pass = registerDto.getPass();
            String passConfirmation = registerDto.getPassConfirmed();
            int passportSeries = StringTypeVerifier.integer(registerDto.getPassportSeries(),
                    "Серия паспорта должна быть числом");
            int passportNumber = StringTypeVerifier.integer(registerDto.getPassportNumber(),
                    "Номер паспорта должен быть числом");
            StringTypeVerifier.notEmptyString(registerDto.getSex(), "Пол должен быть указан");
            StringTypeVerifier.notEmptyString(registerDto.getRole(), "Роль должна быть выбрана");
            if (!pass.equals(passConfirmation)) {
                return new SuccessDto("Введённые пароли не совпадают");
            }
            User user = new User();
            user.setRole(Role.valueOf(registerDto.getRole()));
            user.setLogin(registerDto.getLogin());
            user.setPhone(registerDto.getPhone());
            user.setSurname(registerDto.getSurname());
            user.setName(registerDto.getName());
            user.setPatronymic(registerDto.getPatronymic());
            user.setSex(Sex.valueOf(registerDto.getSex()));
            user.setPassportSeries(passportSeries);
            user.setPassportNumber(passportNumber);
            user.setPassword(registerDto.getPass());
            userRepo.save(user);
            return new SuccessDto();
        } catch (Exception e) {
            return new SuccessDto(e.getMessage());
        }
    }

    @Override
    public Optional<String> login(final String login, final String password) {
        final String token = UUID.randomUUID().toString();
        User u = userRepo.findByLogin(login);
        if (u == null || !u.getPassword().equals(password)) {
            return Optional.empty();
        }
        Token t = new Token();
        t.setId(token);
        t.setUser(u);
        tokenRepo.save(t);
        return Optional.of(token);
    }

    @Override
    public Optional<User> findByToken(final String token) {
        var userOpt = tokenRepo.findById(token);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userOpt.get().getUser());
    }

    @Override
    public void logout(final User user) {

    }
}

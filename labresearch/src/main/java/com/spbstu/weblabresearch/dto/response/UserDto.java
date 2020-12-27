package com.spbstu.weblabresearch.dto.response;

import com.spbstu.weblabresearch.dbo.Role;
import com.spbstu.weblabresearch.dbo.Sex;
import com.spbstu.weblabresearch.dbo.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static lombok.AccessLevel.PRIVATE;

/**
 *
 */
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class UserDto {
    int id;
    String login;
    String surname;
    String name;
    String patronymic;
    Sex sex;
    int passportSeries;
    int passportNumber;
    Role role;
    String phone;

    public UserDto(User user) {
        id = user.getId();
        login = user.getLogin();
        surname = user.getSurname();
        name = user.getName();
        patronymic = user.getPatronymic();
        sex = user.getSex();
        passportSeries = user.getPassportSeries();
        passportNumber = user.getPassportNumber();
        role = user.getRole();
        phone = user.getPhone();
    }
}

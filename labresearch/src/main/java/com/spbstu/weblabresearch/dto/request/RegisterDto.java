package com.spbstu.weblabresearch.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class RegisterDto {
    String login;
    String surname;
    String name;
    String patronymic;
    String sex;
    String passportSeries;
    String passportNumber;
    String phone;
    String role;
    String pass;
    String passConfirmed;
}

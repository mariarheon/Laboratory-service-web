package com.spbstu.weblabresearch.dto.common;

import com.spbstu.weblabresearch.dbo.*;
import com.spbstu.weblabresearch.dto.response.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 *
 */
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class RequestDto {
    int id;
    String surname;
    String name;
    String patronymic;
    Sex sex;
    int passportSeries;
    int passportNumber;
    Date arrivalTime;
    RequestStatus status;
    UserDto client;
    List<Analysis> analysisList;
    String er;

    public RequestDto(String er) {
        this.er = er;
    }

    public RequestDto(Request request) {
        id = request.getId();
        surname = request.getSurname();
        name = request.getName();
        patronymic = request.getPatronymic();
        sex = request.getSex();
        passportSeries = request.getPassportSeries();
        passportNumber = request.getPassportNumber();
        arrivalTime = request.getArrivalTime();
        status = request.getStatus();
        client = new UserDto(request.getClient());
        analysisList = request.getAnalysisList();
    }
}

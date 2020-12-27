package com.spbstu.weblabresearch.dbo;

import com.spbstu.weblabresearch.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 *
 */
@Entity
@Data
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String surname;

    String name;

    String patronymic;

    @Enumerated(EnumType.STRING)
    Sex sex;

    int passportSeries;

    int passportNumber;

    Date arrivalTime;

    @Enumerated(EnumType.STRING)
    RequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    User client;

    @ManyToMany
    @JoinTable(
            name = "request_analysis_link",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "analysis_id")
    )
    List<Analysis> analysisList;
}

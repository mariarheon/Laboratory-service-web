package com.spbstu.weblabresearch.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Data
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_id")
    private User assistant;

    @Enumerated(EnumType.STRING)
    private Weekday weekday;

    private Time startTime;

    private Time endTime;
}

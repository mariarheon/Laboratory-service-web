package com.spbstu.weblabresearch.dbo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 *
 */
@Entity
@Data
@NoArgsConstructor
public class Busy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_id")
    private User assistant;

    private Date theDate;

    private Time startTime;

    private Time endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    @JsonIgnore
    @ToString.Exclude
    private Form form;

    @Enumerated(EnumType.STRING)
    private Reason reason;
}

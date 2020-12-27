package com.spbstu.weblabresearch.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 */
@Entity
@Data
@NoArgsConstructor
public class FormField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    private int orderNum;

    private String name;

    private String description;

    private String units;
}

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
public class Protocol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    private String data;
}

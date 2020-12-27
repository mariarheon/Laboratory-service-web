package com.spbstu.weblabresearch.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldDto {
    private int id;
    private String description;
    private String units;
    private String value;

    public FieldDto(int id, String description, String units, String value) {
        this.id = id;
        this.description = description;
        this.units = units;
        this.value = value;
    }
}

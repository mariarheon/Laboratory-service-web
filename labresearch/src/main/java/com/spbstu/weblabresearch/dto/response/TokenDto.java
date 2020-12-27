package com.spbstu.weblabresearch.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    String token;
    String er;

    public TokenDto(String er) {
        this.er = er;
    }
}

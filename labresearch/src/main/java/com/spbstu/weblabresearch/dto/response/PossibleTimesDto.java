package com.spbstu.weblabresearch.dto.response;

import com.spbstu.weblabresearch.dbo.TimeSpan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class PossibleTimesDto {
    List<TimeSpan> timeIntervals;
    String er;

    public PossibleTimesDto(String er) {
        this.er = er;
    }

    public PossibleTimesDto(List<TimeSpan> timeIntervals) {
        this.timeIntervals = timeIntervals;
    }
}
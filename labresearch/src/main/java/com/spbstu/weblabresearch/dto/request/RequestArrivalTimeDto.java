package com.spbstu.weblabresearch.dto.request;

import com.spbstu.weblabresearch.dbo.Time;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class RequestArrivalTimeDto {
    int requestId;
    int hh;
    int mm;

    public Time getTime() {
        return new Time(hh, mm);
    }
}

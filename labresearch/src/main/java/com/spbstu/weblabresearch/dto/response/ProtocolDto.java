package com.spbstu.weblabresearch.dto.response;

import com.spbstu.weblabresearch.dbo.Form;
import com.spbstu.weblabresearch.dbo.Protocol;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 *
 */
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class ProtocolDto {
    String protocol;
    String er;

    public ProtocolDto(Protocol protocol) {
        this.protocol = protocol.getData();
    }

    public ProtocolDto(String er) {
        this.er = er;
    }
}

package com.spbstu.weblabresearch.dto.response;

import com.spbstu.weblabresearch.dbo.Form;
import com.spbstu.weblabresearch.dbo.Role;
import com.spbstu.weblabresearch.dbo.Sex;
import com.spbstu.weblabresearch.dbo.User;
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
public class FormDto {
    int id;
    Date collectionStartTime;
    Date collectionEndTime;
    Date researchStartTime;
    Date researchEndTime;
    String barcode;
    String requestSurname;
    String requestName;
    String requestPatronymic;
    String analysis;
    String er;
    List<FieldDto> fields;

    public FormDto(Form form, boolean withFields) {
        id = form.getId();
        collectionStartTime = form.getCollectionStartTime();
        collectionEndTime = form.getCollectionEndTime();
        researchStartTime = form.getResearchStartTime();
        researchEndTime = form.getResearchEndTime();
        barcode = form.getBarcode();
        requestSurname = form.getRequest().getSurname();
        requestName = form.getRequest().getName();
        requestPatronymic = form.getRequest().getPatronymic();
        analysis = form.getAnalysis().getName();
        if (withFields) {
            addFields(form);
        }
    }

    private void addFields(Form form) {
        fields = form.getFormFieldLinks()
                .stream()
                .sorted(Comparator.comparingInt(x -> x.getField().getOrderNum()))
                .map(x -> {
                    var id = x.getField().getId();
                    var desc = x.getField().getDescription();
                    var units = x.getField().getUnits();
                    var val = x.getValue();
                    return new FieldDto(id, desc, units, val);
                })
                .collect(Collectors.toList());
    }

    public FormDto(String er) {
        this.er = er;
    }
}

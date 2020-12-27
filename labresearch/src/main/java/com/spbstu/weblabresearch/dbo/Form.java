package com.spbstu.weblabresearch.dbo;

import com.spbstu.weblabresearch.util.EAN13Generator;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@Entity
@Data
public class Form {
    private static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_id")
    private User assistant;

    private String barcode;

    @Enumerated(value = EnumType.STRING)
    private FormStatus status;

    @OneToMany(
            mappedBy = "form",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FormFieldLink> formFieldLinks;

    @OneToMany(
            mappedBy = "form",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Busy> busyList;

    public Form() {
        barcode = EAN13Generator.generate();
        status = FormStatus.IN_WORK;
    }

    public void setCollectionTime(java.util.Date startTime, java.util.Date endTime) {
        Optional<Busy> busyOpt;
        if (busyList == null) {
            busyList = new LinkedList<Busy>();
            busyOpt = Optional.empty();
        } else {
            busyOpt = busyList
                    .stream()
                    .filter(x -> x.getReason() == Reason.COLLECTION)
                    .findFirst();
        }
        Busy busy;
        if (busyOpt.isEmpty()) {
            busy = new Busy();
            busy.setForm(this);
            busy.setAssistant(this.getAssistant());
            busy.setReason(Reason.COLLECTION);
        } else {
            busy = busyOpt.get();
        }
        busy.setTheDate(new java.sql.Date(startTime.getTime()));
        busy.setStartTime(new java.sql.Time(startTime.getTime()));
        busy.setEndTime(new java.sql.Time(endTime.getTime()));
        if (busyOpt.isEmpty()) {
            busyList.add(busy);
        }
    }

    public void setResearchTime(java.util.Date startTime, java.util.Date endTime) {
        Optional<Busy> busyOpt;
        if (busyList == null) {
            busyList = new LinkedList<Busy>();
            busyOpt = Optional.empty();
        } else {
            busyOpt = busyList
                    .stream()
                    .filter(x -> x.getReason() == Reason.RESEARCH)
                    .findFirst();
        }
        Busy busy;
        if (busyOpt.isEmpty()) {
            busy = new Busy();
            busy.setForm(this);
            busy.setAssistant(this.getAssistant());
            busy.setReason(Reason.RESEARCH);
        } else {
            busy = busyOpt.get();
        }
        busy.setTheDate(new java.sql.Date(startTime.getTime()));
        busy.setStartTime(new java.sql.Time(startTime.getTime()));
        busy.setEndTime(new java.sql.Time(endTime.getTime()));
        if (busyOpt.isEmpty()) {
            busyList.add(busy);
        }
    }

    public java.util.Date getCollectionStartTime() {
        if (busyList == null) {
            return null;
        }
        var busyOpt = busyList
                .stream()
                .filter(x -> x.getReason() == Reason.COLLECTION)
                .findFirst();
        if (busyOpt.isEmpty()) {
            return null;
        }
        Busy busy = busyOpt.get();
        return mergeDateAndTime(busy.getTheDate(), busy.getStartTime());
    }

    public java.util.Date getCollectionEndTime() {
        if (busyList == null) {
            return null;
        }
        var busyOpt = busyList
                .stream()
                .filter(x -> x.getReason() == Reason.COLLECTION)
                .findFirst();
        if (busyOpt.isEmpty()) {
            return null;
        }
        Busy busy = busyOpt.get();
        return mergeDateAndTime(busy.getTheDate(), busy.getEndTime());
    }

    public java.util.Date getResearchStartTime() {
        if (busyList == null) {
            return null;
        }
        var busyOpt = busyList
                .stream()
                .filter(x -> x.getReason() == Reason.RESEARCH)
                .findFirst();
        if (busyOpt.isEmpty()) {
            return null;
        }
        Busy busy = busyOpt.get();
        return mergeDateAndTime(busy.getTheDate(), busy.getStartTime());
    }

    public java.util.Date getResearchEndTime() {
        if (busyList == null) {
            return null;
        }
        var busyOpt = busyList
                .stream()
                .filter(x -> x.getReason() == Reason.RESEARCH)
                .findFirst();
        if (busyOpt.isEmpty()) {
            return null;
        }
        Busy busy = busyOpt.get();
        return mergeDateAndTime(busy.getTheDate(), busy.getEndTime());
    }

    private java.util.Date mergeDateAndTime(java.sql.Date date, java.sql.Time time) {
        // Construct date and time objects
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        // Extract the time of the "time" object to the "date"
        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

        // Get the time value!
        return dateCal.getTime();
    }

    /*
    private Date collectionStartDateTime;

    private Date collectionEndDateTime;

    private Date researchStartDateTime;

    private Date researchEndDateTime;

    private static String dateFormatted(Date date) {
        if (date == null) {
            return "Неизвестно";
        }
        return format.format(date);
    }

    private String getCollectionStartDateTimeFormatted() {
        return dateFormatted(collectionStartDateTime);
    }

    private String getCollectionEndDateTimeFormatted() {
        return dateFormatted(collectionEndDateTime);
    }

    private String getResearchStartDateTimeFormatted() {
        return dateFormatted(researchStartDateTime);
    }

    private String getResearchEndDateTimeFormatted() {
        return dateFormatted(researchEndDateTime);
    }

    public Date getCollectionStartDateTime() {
        return collectionStartDateTime;
    }

    public void setCollectionStartDateTime(Date collectionStartDateTime) {
        this.collectionStartDateTime = collectionStartDateTime;
    }

    public Date getCollectionEndDateTime() {
        return collectionEndDateTime;
    }

    public void setCollectionEndDateTime(Date collectionEndDateTime) {
        this.collectionEndDateTime = collectionEndDateTime;
    }

    public Date getResearchStartDateTime() {
        return researchStartDateTime;
    }

    public void setResearchStartDateTime(Date researchStartDateTime) {
        this.researchStartDateTime = researchStartDateTime;
    }

    public Date getResearchEndDateTime() {
        return researchEndDateTime;
    }

    public void setResearchEndDateTime(Date researchEndDateTime) {
        this.researchEndDateTime = researchEndDateTime;
    }
     */
}

/*
 * Copyright 2014-05-11 the original author or authors.
 */

package pl.com.softproject.carelinkexporter.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import pl.com.softproject.carelinkexporter.util.DurationFormatter;

/**
 *
 * @author Adrian Lapierre <adrian@softproject.com.pl>
 */
public class Mensuration {
    
    private int lp;
    private LocalDateTime date;
    private RecordType recordType;
    private int glucose;
    private BolusType bolusType;
    private Double bolusValue;
    private Double deleyedBolusValue;
    private Duration bolusTime;
    

    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public BolusType getBolusType() {
        return bolusType;
    }

    public void setBolusType(BolusType bolusType) {
        this.bolusType = bolusType;
    }

    public Double getBolusValue() {
        return bolusValue;
    }

    public void setBolusValue(Double bolusValue) {
        this.bolusValue = bolusValue;
    }

    public Duration getBolusTime() {
        return bolusTime;
    }

    public void setBolusTime(Duration bolusTime) {
        this.bolusTime = bolusTime;
    }

    public Double getDeleyedBolusValue() {
        return deleyedBolusValue;
    }

    public void setDeleyedBolusValue(Double deleyedBolusValue) {
        this.deleyedBolusValue = deleyedBolusValue;
    }

    public LocalDate getRecordDate() {
        return date.toLocalDate();
    }
    
    public LocalTime getRecordTime() {
        return date.toLocalTime();
    }
    
    @Override
    public String toString() {
        return "Mensuration{" + "lp=" + lp + ", date=" + date + ", recordType=" + recordType + ", glucose=" + glucose + ", bolusType=" + bolusType + ", bolusValue=" + bolusValue + ", deleyedBolusValue=" + deleyedBolusValue + ", bolusTime=" + DurationFormatter.format(bolusTime) + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.date);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mensuration other = (Mensuration) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }

    
    
}

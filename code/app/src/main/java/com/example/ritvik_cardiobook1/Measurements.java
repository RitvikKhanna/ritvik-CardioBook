package com.example.ritvik_cardiobook1;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/*
 * @author Ritvik Khanna
 * @ccid ritvik
 * Copyright 2019, Ritvik khanna
 * @github RitvikKhanna
 * The measurement class is definition of all the measurement type objects
 * The measurement object includes
 * Date, Time, Systolic & Diastolic Pressure, Heart Rate and comments if any
 * We have getters() and setters() for them
 */

class Measurements implements Serializable {

    private String date;
    private String time;
    private String systolicPressure;
    private String diastolicPressure;
    private String heartRate;
    private String comment;

    public Measurements() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSystolicPressure(String systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public void setDiastolicPressure(String diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSystolicPressure() {
        return systolicPressure;
    }

    public String getDiastolicPressure() {
        return diastolicPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public String getComment() {
        return comment;
    }

}




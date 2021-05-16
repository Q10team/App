package com.example.teamproject.TodoList;

import java.io.Serializable;

public class DateData implements Serializable {
    private int year;
    private int month;
    private int day;

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    public String datetimeConverter(int year, int month, int day) {
        String smonth = String.valueOf(month);
        String sday = String.valueOf(day);
        if(Integer.parseInt(smonth) < 10)
            smonth = "0" + smonth;
        if(Integer.parseInt(sday) < 10)
            sday = "0" + sday;
        return year +"-"+ smonth + "-" + sday + " 00:00:00";
    }

    public String datetimeConverter(String syear, String smonth, String sday) {
        if(Integer.parseInt(smonth) < 10)
            smonth = "0" + smonth;
        if(Integer.parseInt(sday) < 10)
            sday = "0" + sday;
        return syear +"-"+ smonth + "-" + sday + " 00:00:00";
    }

    public DateData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateData() {}
}
package com.boushib.eventsismagi.model;

import java.util.Calendar;

public class CalendarDay {
    private int day;
    private boolean isOtherMonth;

    public CalendarDay(int day, boolean isOtherMonth) {
        this.day = day;
        this.isOtherMonth = isOtherMonth;
    }

    public int getDay() {
        return day;
    }

    public boolean isOtherMonth() {
        return isOtherMonth;
    }
}

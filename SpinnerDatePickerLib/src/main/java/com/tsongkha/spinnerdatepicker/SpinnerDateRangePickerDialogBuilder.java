package com.tsongkha.spinnerdatepicker;

import android.content.Context;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SpinnerDateRangePickerDialogBuilder {

    private Context context;
    private DateRangePickerDialog.OnRangeDateSetListener callBack;
    private boolean isDayShown = true;
    private boolean isTitleShown = true;
    private int theme = -1;                 //default theme
    private int spinnerTheme = -1;          //default theme
    private Calendar defaultDate = new GregorianCalendar(1980, 0, 1);
    private Calendar minDate = new GregorianCalendar(1900, 0, 1);
    private Calendar maxDate = new GregorianCalendar(2100, 0, 1);
    private String titleString;
    private String toTitleString;
    private String fromTitleString;


    public SpinnerDateRangePickerDialogBuilder context(Context context) {
        this.context = context;
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder callback(DateRangePickerDialog.OnRangeDateSetListener callBack) {
        this.callBack = callBack;
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder dialogTheme(int theme) {
        this.theme = theme;
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder spinnerTheme(int spinnerTheme) {
        this.spinnerTheme = spinnerTheme;
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder defaultDate(int year, int monthIndexedFromZero, int day) {
        this.defaultDate = new GregorianCalendar(year, monthIndexedFromZero, day);
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder minDate(int year, int monthIndexedFromZero, int day) {
        this.minDate = new GregorianCalendar(year, monthIndexedFromZero, day);
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder maxDate(int year, int monthIndexedFromZero, int day) {
        this.maxDate = new GregorianCalendar(year, monthIndexedFromZero, day);
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder setCurrentDateAsMaxDate() {
        this.maxDate =  Calendar.getInstance();
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder setMinDateFromMaxDateInterval(int yearDiff)
    {
        if(this.maxDate==null)
            this.maxDate =  Calendar.getInstance();
        this.minDate=new GregorianCalendar(maxDate.get(Calendar.YEAR)-yearDiff,this.maxDate.get(Calendar.MONTH),this.maxDate.get(Calendar.DAY_OF_MONTH));
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder showDaySpinner(boolean showDaySpinner) {
        this.isDayShown = showDaySpinner;
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder showTitle(boolean showTitle) {
        this.isTitleShown = showTitle;
        return this;
    }


    public SpinnerDateRangePickerDialogBuilder setTitle(String title) {
        this.titleString = title;
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder setToTitle(String title)
    {
        this.toTitleString=title;
        return this;
    }

    public SpinnerDateRangePickerDialogBuilder setFromTitle(String title)
    {
        this.fromTitleString=title;
        return this;
    }


    public DateRangePickerDialog build() {
        if (context == null) throw new IllegalArgumentException("Context must not be null");
        if (maxDate.getTime().getTime() <= minDate.getTime().getTime()) throw new IllegalArgumentException("Max date is not after Min date");

        return new DateRangePickerDialog(context, theme, spinnerTheme, callBack, defaultDate, minDate, maxDate, isDayShown, isTitleShown,titleString,fromTitleString,toTitleString);
    }
}
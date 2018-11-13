package com.tsongkha.spinnerdatepickerexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.DateRangePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.tsongkha.spinnerdatepicker.SpinnerDateRangePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by rawsond on 25/08/17.
 */

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DateRangePickerDialog.OnRangeDateSetListener {

    TextView dateTextView;
    TextView dateToTextView;
    Button dateButton;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateButton = (Button) findViewById(R.id.set_date_button);
        dateTextView = (TextView) findViewById(R.id.date_textview);
        dateToTextView = (TextView) findViewById(R.id.date_totextview);
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate(1980, 0, 1, R.style.DatePickerSpinner);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }


    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
//        new SpinnerDatePickerDialogBuilder()
//                .context(MainActivity.this)
//                .spinnerTheme(R.style.NumberPickerStyle)
//                .showTitle(true)
//                .showDaySpinner(true)
//                .defaultDate(1990, 0, 1)
//                .maxDate(2020, 0, 1)
//                .minDate(1900, 0, 1)
//                .dialogTheme(R.style.DatePickerTheme)
//                .showTitle(true)
//                .setTitle("Date of Birth")
//                .callback(MainActivity.this)
//                .build()
//                .show();

        new SpinnerDateRangePickerDialogBuilder()
                .context(MainActivity.this)
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(1990, 0, 1)
                .setCurrentDateAsMaxDate()
                .minDate(2017,10,10)
                .dialogTheme(R.style.DatePickerTheme)
                .showTitle(true)
                .setTitle("Filter by date")
                .callback(MainActivity.this)
                .build()
                .show();
    }

    @Override
    public void onFromDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onToDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateToTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onInvalidRangeSelected() {
        Toast.makeText(this, "Invalid date range selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateRangeReceivedSucess() {

    }
}
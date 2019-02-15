package com.tsongkha.spinnerdatepicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A fork of the Android Open Source Project DatePickerDialog class
 */
public class DateRangePickerDialog extends AlertDialog implements OnClickListener, View.OnClickListener {

    private static final String FROM_YEAR = "fyear";
    private static final String FROM_MONTH = "fmonth";
    private static final String FROM_DAY = "fday";

    private static final String TO_YEAR = "tyear";
    private static final String TO_MONTH = "tmonth";
    private static final String TO_DAY = "tday";
    private static final String SHOULD_RANGE_TITLE_NEED_TO_BE_SHOWN = "title_enabled";
    private static final String RANGE_PICKER_TITLE = "range_picker_title";

    private final DatePicker fromDatePicker;
    private final DatePicker toDatePicker;
    private final OnRangeDateSetListener mCallBack;
    private final DateFormat mTitleDateFormat;
    private final float pixelToDp;
    private static Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private String mTitleText;

    private boolean mIsDayShown = true;
    private boolean updateRangeTitle = true;

    private OnFromDateChanged onFromDateChanged;
    private OnToDateChanged onToDateChanged;

    private TextView fromDateTitle = null;
    private TextView toDateTitle = null;

    private View positiveButton;


    /**
     * The datePickerCallback used to indicate the user is done filling in the date.
     */
    public interface OnRangeDateSetListener {
        /**
         * @param view        The view associated with this listener.
         * @param year        The year that was set
         * @param monthOfYear The month that was set (0-11) for compatibility
         *                    with {@link Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onFromDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);

        void onToDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);

        void onInvalidRangeSelected();

        void onDateRangeReceivedSucess();
    }

    DateRangePickerDialog(Context context,
                          int theme,
                          int spinnerTheme,
                          OnRangeDateSetListener callBack,
                          Calendar defaultDate,
                          Calendar minDate,
                          Calendar maxDate,
                          boolean isDayShown,
                          boolean isTitleShown,
                          String title,
                          String fromTitleTextString,
                          String toTitleTextString,
                          String positiveButtonText,
                          String negativeButtonText) {
        super(context, theme);

        pixelToDp = context.getResources().getDisplayMetrics().density;
        mCallBack = callBack;
        mTitleDateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        mIsDayShown = isDayShown;
        updateRangeTitle = isTitleShown;
        mTitleText = title;

        updateTitle();

        if(positiveButtonText==null||positiveButtonText.length()<1)
            positiveButtonText=context.getString(android.R.string.ok);

        if(negativeButtonText==null||negativeButtonText.length()<1)
            negativeButtonText=context.getString(android.R.string.cancel);

        setButton(BUTTON_POSITIVE, positiveButtonText,
                (OnClickListener) null);


        setButton(BUTTON_NEGATIVE, negativeButtonText,
                (OnClickListener) null);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_range_picker_dialog_container, null);
        setView(view);

        fromDateTitle = view.findViewById(R.id.fromDateText);
        toDateTitle = view.findViewById(R.id.toDateText);

        if (fromTitleTextString != null && fromTitleTextString.length() > 0)
            fromDateTitle.setText(fromTitleTextString);

        if (toTitleTextString != null && toTitleTextString.length() > 0)
            toDateTitle.setText(toTitleTextString);

        onFromDateChanged = new OnFromDateChanged();
        onToDateChanged = new OnToDateChanged();

        fromDatePicker = new DatePicker((ViewGroup) view, spinnerTheme, 1);
        fromDatePicker.setMinDate(minDate.getTimeInMillis());
        fromDatePicker.setMaxDate(maxDate.getTimeInMillis());
//        fromDatePicker.init(defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH), defaultDate.get(Calendar.DAY_OF_MONTH), isDayShown, onFromDateChanged);
        fromDatePicker.init(minDate.get(Calendar.YEAR), minDate.get(Calendar.MONTH), minDate.get(Calendar.DAY_OF_MONTH), isDayShown, onFromDateChanged);

        toDatePicker = new DatePicker((ViewGroup) view, spinnerTheme, 3);
        toDatePicker.setMinDate(minDate.getTimeInMillis());
        toDatePicker.setMaxDate(maxDate.getTimeInMillis());
//        toDatePicker.init(defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH), defaultDate.get(Calendar.DAY_OF_MONTH), isDayShown, onToDateChanged);
        toDatePicker.init(maxDate.get(Calendar.YEAR), maxDate.get(Calendar.MONTH), maxDate.get(Calendar.DAY_OF_MONTH), isDayShown, onToDateChanged);

    }

    @Override
    protected void onStart() {
        super.onStart();
        positiveButton = getButton(BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        positiveButton.setOnClickListener(null);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    @Override
    public void onClick(View v) {
        if (mCallBack != null) {
            if (validateSelectedDate()) {
                fromDatePicker.clearFocus();
                toDatePicker.clearFocus();
                mCallBack.onFromDateSet(fromDatePicker, fromDatePicker.getYear(),
                        fromDatePicker.getMonth(), fromDatePicker.getDayOfMonth());
                mCallBack.onToDateSet(toDatePicker, toDatePicker.getYear(),
                        toDatePicker.getMonth(), toDatePicker.getDayOfMonth());
                mCallBack.onDateRangeReceivedSucess();
                this.dismiss();
            } else
                mCallBack.onInvalidRangeSelected();
        }
    }

    private boolean validateSelectedDate() {
        Calendar fromDate = new GregorianCalendar(fromDatePicker.getYear(), fromDatePicker.getMonth(), fromDatePicker.getDayOfMonth());
        Calendar toDate = new GregorianCalendar(toDatePicker.getYear(), toDatePicker.getMonth(), toDatePicker.getDayOfMonth());
        return fromDate.getTimeInMillis() <= toDate.getTimeInMillis();
    }


    private void updateTitle() {
        if (mTitleText != null && mTitleText.length() > 0)
            setTitle(mTitleText);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();

        state.putInt(FROM_YEAR, fromDatePicker.getYear());
        state.putInt(FROM_MONTH, fromDatePicker.getMonth());
        state.putInt(FROM_DAY, fromDatePicker.getDayOfMonth());

        state.putInt(TO_YEAR, fromDatePicker.getYear());
        state.putInt(TO_MONTH, fromDatePicker.getMonth());
        state.putInt(TO_DAY, fromDatePicker.getDayOfMonth());

        state.putBoolean(SHOULD_RANGE_TITLE_NEED_TO_BE_SHOWN, updateRangeTitle);

        state.putString(RANGE_PICKER_TITLE, mTitleText);

        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        updateRangeTitle = savedInstanceState.getBoolean(SHOULD_RANGE_TITLE_NEED_TO_BE_SHOWN);
        mTitleText = savedInstanceState.getString(RANGE_PICKER_TITLE);

        int fYear = savedInstanceState.getInt(FROM_YEAR);
        int fMonth = savedInstanceState.getInt(FROM_MONTH);
        int fDay = savedInstanceState.getInt(FROM_DAY);

        Calendar fc = Calendar.getInstance();
        fc.set(Calendar.YEAR, fYear);
        fc.set(Calendar.MONTH, fMonth);
        fc.set(Calendar.DAY_OF_MONTH, fDay);
//        updateFromTitle(fc);

        int tYear = savedInstanceState.getInt(TO_YEAR);
        int tMonth = savedInstanceState.getInt(TO_MONTH);
        int tDay = savedInstanceState.getInt(TO_DAY);

        Calendar tc = Calendar.getInstance();
        tc.set(Calendar.YEAR, fYear);
        tc.set(Calendar.MONTH, fMonth);
        tc.set(Calendar.DAY_OF_MONTH, fDay);
//        updateToTitle(tc);

        updateTitle();

        fromDatePicker.init(fYear, fMonth, fDay, mIsDayShown, onFromDateChanged);
        toDatePicker.init(tYear, tMonth, tDay, mIsDayShown, onToDateChanged);
    }

    public class OnFromDateChanged implements OnDateChangedListener {

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar updatedDate = Calendar.getInstance();
            updatedDate.set(Calendar.YEAR, year);
            updatedDate.set(Calendar.MONTH, monthOfYear);
            updatedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        }
    }

    public class OnToDateChanged implements OnDateChangedListener {

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar updatedDate = Calendar.getInstance();
            updatedDate.set(Calendar.YEAR, year);
            updatedDate.set(Calendar.MONTH, monthOfYear);
            updatedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        }
    }
}
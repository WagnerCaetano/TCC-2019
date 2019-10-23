package com.example.islidee;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DurationPicker extends TimePickerDialog {

    private TimePicker timePicker;
    private final OnTimeSetListener callback;

    private int initialMinutes;

    public DurationPicker(Context context, OnTimeSetListener callBack,int minutes, int seconds) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, minutes,
                seconds, true);
        //super(context, callBack, minutes, seconds, true);
        this.callback = callBack;
        this.initialMinutes = minutes;
        this.setTitle("Set duration");

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (callback != null && timePicker != null) {
            timePicker.clearFocus();
            callback.onTimeSet(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            this.timePicker = (TimePicker)findViewById(R.id.TimePicker);
            Field field = classForid.getField("hour");

            int maxMinutes = 60;
            NumberPicker mHourSpinner = (NumberPicker)timePicker.findViewById(field.getInt(null));
            mHourSpinner.setMinValue(0);
            mHourSpinner.setMaxValue(maxMinutes);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i <= maxMinutes; i++) {
                displayedValues.add(String.format("%d", i));
            }
            mHourSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
            mHourSpinner.setValue(initialMinutes); // we can set this again now that we've modified the hours spinner

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
package com.example.alarmapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "time setting";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hourOfDay, int minute) {
        String output = String.format(Locale.JAPAN, "%2d:%2d", hourOfDay, minute);
        Log.i(TAG, output); // 動作確認ずみ
    }
}

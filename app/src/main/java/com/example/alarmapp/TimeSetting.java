package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.Cache;

import java.util.Calendar;
import java.util.Locale;

public class TimeSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);

        Button timeDecision_button = findViewById(R.id.timeDecision_button);
        timeDecision_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent alarmIntent = getPendingIntent();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 11);

                long alarmTimeMillis = calendar.getTimeInMillis();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, null), alarmIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, alarmIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, alarmIntent);
                }

                boolean alarmUp = (getPendingIntent_NO_CREATE() != null);

                // 確認済み
                if (alarmUp) Log.d("myTag", "=== Alarm is already active ===");
                else Log.d("myTag", "=== Alarm is not active ===");

                Intent intent_timeDecision = new Intent(getApplication(),Home.class);
                startActivity(intent_timeDecision);
            }
        });


    }
    public void showTimePicker(View v){
        DialogFragment newFragment = new TimePicker();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setClass(this, AlarmReceiver.class);

        return PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }
    public PendingIntent getPendingIntent_NO_CREATE() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setClass(this, AlarmReceiver.class);

        return PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_NO_CREATE);
    }
}

// 参考
// https://developer.android.com/guide/topics/ui/controls/pickers?hl=ja#TimePicker
// https://qiita.com/hiroaki-dev/items/e3149e0be5bfa52d6a51
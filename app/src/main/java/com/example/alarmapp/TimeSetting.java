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

    // FIXME
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);

        Button timeDecision_button = findViewById(R.id.timeDecision_button);
        timeDecision_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 7);
                calendar.set(Calendar.MINUTE, 0);


                long alarmTimeMillis = calendar.getTimeInMillis();

//                // test
//                if (alarmIntent != null && alarmManager != null) {
//                    alarmManager.cancel(alarmIntent);
//                }
//                else Log.d("mytag", "TESTTEST");


                boolean alarmUp2 = (PendingIntent.getBroadcast(context, 0,
                        new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);

                // 確認済み
                if (alarmUp2) Log.d("myTag", "==Alarm is already active==");
                else Log.d("myTag", "==Alarm is not active==");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, null), alarmIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, alarmIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, alarmIntent);
                }

                boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);

                // 確認済み
                if (alarmUp) Log.d("myTag", "Alarm is already active");
                else Log.d("myTag", "Alarm is not active");




                Intent intent_timeDecision = new Intent(getApplication(),Home.class);
                startActivity(intent_timeDecision);
            }
        });


    }
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
//
//    }

    public void showTimePicker(View v){
        DialogFragment newFragment = new TimePicker();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}

// 参考
// https://developer.android.com/guide/topics/ui/controls/pickers?hl=ja#TimePicker
// https://qiita.com/hiroaki-dev/items/e3149e0be5bfa52d6a51
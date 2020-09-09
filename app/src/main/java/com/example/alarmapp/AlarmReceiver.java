package com.example.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("myTag", "======Receiver is active======"); //動作を確認
        Intent startAlarmIntent = new Intent(context, Home.class);
        startAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startAlarmIntent.putExtra("flag", true);
        context.startActivity(startAlarmIntent);
    }
}
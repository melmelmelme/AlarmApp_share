package com.example.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("myTag", "======Receiver is active======"); //動作確認済み
        Intent startAlarmIntent = new Intent(context, PlaySoundActivity.class);
        startAlarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startAlarmIntent);
    }
}
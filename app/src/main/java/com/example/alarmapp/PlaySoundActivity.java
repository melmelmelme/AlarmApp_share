package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;

public class PlaySoundActivity extends AppCompatActivity {

    Button stopSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);

        Log.d("myTag", "======PlaySoundActivity======");

        getWindow().addFlags(FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play_sound);

        startService(new Intent(this, PlaySoundService.class));

        stopSound = (Button) findViewById(R.id.stop_button);
        stopSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(PlaySoundActivity.this, PlaySoundService.class));
                PreferenceUtil pref = new PreferenceUtil(PlaySoundActivity.this);
//                pref.delete(); FIXME 多分いらない(要確認)
            }
        });
    }
}
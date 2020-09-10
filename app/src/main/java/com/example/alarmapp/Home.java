package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private float volume = 0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        Intent intent = getIntent();
//        boolean flag = intent.getBooleanExtra("flag", false);

//        if(flag){
//            mediaPlayer = MediaPlayer.create(this, R.raw.sample);
//            mediaPlayer.setVolume(volume, volume);
//            mediaPlayer.start();
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    Log.d("onCompletion", "=== active ===");
//                    volume += 0.1f;
//                    mediaPlayer.setVolume(volume, volume);
//                    mediaPlayer.start();
//                }
//            });
//        }

        Button timeSet_button = findViewById(R.id.timeSet_button);
        timeSet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_timeSet = new Intent(getApplication(),TimeSetting.class);
                startActivity(intent_timeSet);
            }
        });

        Button stop_button = findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
                // FIXME === cloud store に起床したことを表す信号を投げる ===


                Intent intent_stop = new Intent(getApplication(),Stay.class);
                startActivity(intent_stop);
            }
        });
    }

    private void stopAlarm() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}


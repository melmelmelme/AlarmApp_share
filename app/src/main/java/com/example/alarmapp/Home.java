package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                Intent intent_stop = new Intent(getApplication(),Stay.class);
                startActivity(intent_stop);
            }
        });
    }


}
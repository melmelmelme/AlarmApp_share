package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
                Intent intent_timeDecision = new Intent(getApplication(),Home.class);
                startActivity(intent_timeDecision);
            }
        });


    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

    }

    public void showTimePicker(View v){
        DialogFragment newFragment = new TimePicker();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}

// 参考: https://developer.android.com/guide/topics/ui/controls/pickers?hl=ja#TimePicker
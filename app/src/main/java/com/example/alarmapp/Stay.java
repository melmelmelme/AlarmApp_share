package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Stay extends AppCompatActivity {

    private static final String TAG = "count";

    int applause_cnt; // 拍手回数を保持

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay);

        final Button applause_btn = findViewById(R.id.applause_button);
        applause_cnt = 0;
        applause_btn.setOnClickListener(new View.OnClickListener(){
                                            public void onClick(View v) {
                                                applause_cnt += 1;
                                                if(applause_cnt == 10) Log.i(TAG, "onClick"); // 動作確認済み
                                            }
                                        }
                                        );


    }


}
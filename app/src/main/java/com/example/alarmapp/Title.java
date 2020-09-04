package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Title extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);


        Button makeTeam_button = findViewById(R.id.makeAccount_button);
        makeTeam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_makeTeam = new Intent(getApplication(), MakeTeam.class);
                startActivity(intent_makeTeam);
            }
        });

        Button joinTeam_button = findViewById(R.id.joinTeam_button);
        joinTeam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_joinTeam = new Intent(getApplication(), JoinTeam.class);
                startActivity(intent_joinTeam);
            }
        });
    }
}
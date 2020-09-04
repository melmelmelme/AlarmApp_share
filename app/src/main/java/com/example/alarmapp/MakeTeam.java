package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MakeTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        Button teamNameDecision_button = findViewById(R.id.teamNameDecision_button);
        teamNameDecision_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_teamNameDecision = new Intent(getApplication(), Home.class);
                startActivity(intent_teamNameDecision);
            }
        });
    }
}
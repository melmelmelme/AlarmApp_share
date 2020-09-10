package com.example.alarmapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Stay extends AppCompatActivity {

    private static final String TAG = "count";

    //メディアプレイヤーのインスタンスの？
    private MediaPlayer mediaPlayer;

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;

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

        //インスタンスの初期化
        /*db = FirebaseFirestore.getInstance();

        //スナップショットによるリアルタイムアップデートの取得
        final DocumentReference docRef = db.collection("cities").document("SF");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });*/



    }

    /*private void stopAlarm() {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/





}
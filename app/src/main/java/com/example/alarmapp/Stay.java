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
    private float volume = 0.3f;

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;

    int applause_cnt; // 拍手回数を保持

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay);

        mediaPlayer = MediaPlayer.create(this, R.raw.sample);
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("onCompletion", "=== active ===");
                volume += 0.1f;
                mediaPlayer.setVolume(volume, volume);
                mediaPlayer.start();
            }
        });

        final Button applause_btn = findViewById(R.id.applause_button);
        applause_cnt = 0;
        applause_btn.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                applause_cnt += 1;
                                                if (applause_cnt == 1) {
                                                    Log.i(TAG, "=== (cnt == 1) ==="); // 動作確認済み
                                                    // FIXME 起きたよ～を送信
                                                    stopAlarm(); // テスト用ストップボタン
                                                }
                                            }
                                        }
        );
    }

    private void stopAlarm() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



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


    /*private void stopAlarm() {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/





}
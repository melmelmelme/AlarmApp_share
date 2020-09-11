package com.example.alarmapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Stay extends AppCompatActivity {

    private static final String TAG = "count";

    //メディアプレイヤーのインスタンスの？
    private MediaPlayer mediaPlayer;
    private float volume = 0.3f;

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;

    Map<String, Object> member_c = new HashMap<>();

    //FirebaseAuthのプライベートメンバ変数
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private  String uid = user.getUid();

    int applause_cnt; // 拍手回数を保持
    int count;
    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay);

        //インスタンスの初期化
        db = FirebaseFirestore.getInstance();

        mediaPlayer = MediaPlayer.create(this, R.raw.sample2);
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("onCompletion", "=== completionLister is called ===");
//                volume += 0.1f;
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
                    //ボタン押したときの、自分は止めたよ信号
                    member_c.put(uid, "true");

                    //グループ名の取り出し
                    DocumentReference docRef = db.collection("users").document(uid);//処理落ち関係なし
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String group_name = document.get("group").toString();
                                //サブコレクションの更新
                                db.collection("group").document(group_name)
                                        .collection("member").document("member")
                                        .update(member_c)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void avoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                                //showDialog("作成成功");
                                                //Intent intent_teamNameDecision = new Intent(getApplication(), Home.class);
                                                //startActivity(intent_teamNameDecision);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener(){
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Error writting document", e);
                                                //showDialog("作成失敗");
                                            }
                                        });
                            }else{
                                Log.d(TAG, "Error getting document");
                            }
                        }
                    });

                    //stopAlarm(); // テスト用ストップボタン
                }
                if(applause_cnt % 30 == 0 && applause_cnt >= 30) {
                    volume += 0.3f;
                    if(volume > 1) volume = 1;
                    Log.d("applause", "=== applause_cnt %= 30 ===");
                }
            }
        });




        //グループ名の取り出し
        DocumentReference docRef = db.collection("users").document(uid);//処理落ち関係なし
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    final String group_name = document.get("group").toString();

                    //スナップショットの作成
                    final DocumentReference docRef = db.collection("group").document(group_name)
                            .collection("member").document("member");
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

                            //アラームの終了判定
                            DocumentReference docRef2 = db.collection("group").document(group_name);
                            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        //member配列を持ってきて、アプリ内に配列として保存
                                        DocumentSnapshot document2 = task.getResult();
                                        String member = document2.get("member").toString();
                                        String new_member = member.replace("[", "").replace("]", "");
                                        final String[] member_split = new_member.split(",", 0);
                                        //showDialog(member_split[0]);

                                        DocumentReference docRef3 = db.collection("group").document(group_name)
                                                .collection("member").document("member");
                                        docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document3 = task.getResult();
                                                    count = 0;
                                                    for(i=0;i<member_split.length;i++) {
                                                        String check = document3.get(member_split[i]).toString();
                                                        //trueになってる→起きたボタン押しているとカウントアップ
                                                        if (check.equals("true")) {
                                                            count++;
                                                        }
                                                    }
                                                    //カウントがメンバーの数になっていたらアラームストップ
                                                    if(count == member_split.length){
                                                        stopAlarm();
                                                    }
                                                }else{
                                                    showDialog("失敗");
                                                }
                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                        }
                    });
                }else{
                    Log.d(TAG, "Error getting document");
                }
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

    //ログイン認証の確認ダイアログ処理
    private void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("閉じる", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }


}

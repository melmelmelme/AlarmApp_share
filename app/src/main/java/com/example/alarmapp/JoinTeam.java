package com.example.alarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.alarmapp.TimeSetting.hour;
import static com.example.alarmapp.TimeSetting.minute;

public class JoinTeam extends AppCompatActivity {

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;

    //データベースへのアクセス用の変数
    Map<String, Object> user_c = new HashMap<>();
    Map<String, Object> member_c = new HashMap<>();
    String check_word; //group_nameのsecret_wordを取り出し

    //FirebaseAuthのプライベートメンバ変数
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private  String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        //インスタンスの初期化
        db = FirebaseFirestore.getInstance();

        Button teamDecision_button = findViewById(R.id.teamDecision_button);
        teamDecision_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group_name = ((TextView)findViewById(R.id.teamName_editText)).getText().toString();
                String secret_word = ((TextView)findViewById(R.id.secret_editText)).getText().toString();
                joinGroup(group_name, secret_word);

            }
        });
    }

    private void joinGroup(final String group_name, final String secret_word) {

        //ドキュメントの取り出し
        DocumentReference docRef = db.collection("group").document(group_name);//処理落ち関係なし
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    check_word = document.get("secret_word").toString();
                    if(secret_word.equals(check_word)){
                        //グループのドキュメントの配列の追加
                        DocumentReference update_arrey = db.collection("group").document(group_name);
                        update_arrey.update("member", FieldValue.arrayUnion(uid));

                        //ユーザがどのグループに入っているかの情報
                        user_c.put("group", group_name);
                        db.collection("users").document(uid)
                                .update(user_c)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                                        //showDialog("参加成功");
                                        //Intent intent_teamDecision = new Intent(getApplication(), Home.class);
                                        //startActivity(intent_teamDecision);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener(){
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w(TAG, "Error writting document", e);
                                        //showDialog("参加失敗");
                                    }
                                });

                        //グループのサブコレクションへのメンバー情報の追加
                        member_c.put(uid, "false");
                        db.collection("group").document(group_name)
                                .collection("member").document("member")
                                .update(member_c)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                                        //showDialog("参加成功");
                                        hour = 7; //FIXME
                                        minute = 28; // FIXME
                                        setAlarmManager(hour, minute);
                                        Intent intent_teamDecision = new Intent(getApplication(), Home.class);
                                        startActivity(intent_teamDecision);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener(){
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w(TAG, "Error writting document", e);
                                        //showDialog("参加失敗");
                                    }
                                });

                    }else{
                        showDialog("参加失敗");
                    }
                    //showDialog(check_word);
                    /*if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());*/
                }else{
                    showDialog("失敗");
                }
            }
        });

    }

    //確認ダイアログ処理
    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void setAlarmManager(int hour, int minute) {

        Context context = getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        long alarmTimeMillis = calendar.getTimeInMillis();

        if (Build.VERSION.SDK_INT >= 23) {
//                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, null), alarmIntent);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMillis, alarmIntent);
            Log.d("my tag", String.valueOf(Build.VERSION.SDK_INT));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, alarmIntent);
            Log.d("my tag", "=== ver.sdk_int (2) ===");

        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, alarmIntent);
            Log.d("my tag", "=== ver.sdk_int (3) ===");

        }
    }
}
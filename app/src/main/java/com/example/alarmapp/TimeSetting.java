package com.example.alarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Cache;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TimeSetting extends AppCompatActivity {

    private static final String TAG = "time setting";

    public static int hour;
    public static int minute;
//    public static int maxApplause_int;
    public static int applauseTime_int;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;


    Map<String, Object> time_c = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);

        //インスタンスの初期化
        db = FirebaseFirestore.getInstance();

        Button timeDecision_button = findViewById(R.id.timeDecision_button);
        timeDecision_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);

                // 確認済み
                if (alarmUp) Log.d("myTag", "===== Alarm is already active =====");
                else Log.d("myTag", "Alarm is not active");

//                EditText maxApplause = findViewById(R.id.maxApplause);
                EditText applauseTime = findViewById(R.id.applauseFinishTime);

//                maxApplause_int = Integer.parseInt(maxApplause.getText().toString());
                applauseTime_int = Integer.parseInt(applauseTime.getText().toString());

                //Cloud Firestore上にデータを格納、グループ名はドキュメント名に
                time_c.put("limit_time", applauseTime_int);

                //制限時間の格納
                DocumentReference docRef = db.collection("users").document(uid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String group_name = document.get("group").toString();
                            db.collection("group").document(group_name)
                                    .update(time_c)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void avoid) {
                                            Log.d(TAG, "successfully set time");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener(){
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Error writting document", e);
                                        }
                                    });
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


                Intent intent_timeDecision = new Intent(getApplication(), Home.class);
                startActivity(intent_timeDecision);
            }
        });


    }
    public void showTimePicker(View v){
        DialogFragment newFragment = new TimePicker();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}

// 参考
// https://developer.android.com/guide/topics/ui/controls/pickers?hl=ja#TimePicker
// https://qiita.com/hiroaki-dev/items/e3149e0be5bfa52d6a51
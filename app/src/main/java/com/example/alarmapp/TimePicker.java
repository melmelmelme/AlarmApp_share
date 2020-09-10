package com.example.alarmapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "time setting";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;


    Map<String, Object> time_c = new HashMap<>();


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hourOfDay, int minute) {
//        String output = String.format(Locale.JAPAN, "%2d:%2d", hourOfDay, minute);
//        Log.i(TAG, output); // 動作確認ずみ
        TimeSetting.hour = hourOfDay;
        TimeSetting.minute = minute;

        //インスタンスの初期化
        db = FirebaseFirestore.getInstance();

        // FIXME データベースに時間を投げる
        //Cloud Firestore上にデータを格納、グループ名はドキュメント名に
        time_c.put("hour", hourOfDay);
        time_c.put("minute", minute); //ここで必要なもの入れる

        Log.d(TAG, uid);

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


    }
}

package com.example.alarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MakeTeam extends AppCompatActivity {

    private static final String TAG = "check_tag";
    String name_check;

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;

    //データベースへのアクセス用の変数
    Map<String, Object> user_c = new HashMap<>();
    Map<String, Object> member_c = new HashMap<>();

    //FirebaseAuthのプライベートメンバ変数
    //private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        //インスタンスの初期化
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        Log.d(TAG, uid);

        Button teamNameDecision_button = findViewById(R.id.teamNameDecision_button);
        teamNameDecision_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group_name = ((TextView)findViewById(R.id.newTeamName_editText)).getText().toString();
                String secret_word = ((TextView)findViewById(R.id.secretWord_editText)).getText().toString();
                createGroup(group_name, secret_word);

            }
        });
    }

    private void createGroup(String group_name, String secret_word) {
        //ドキュメントの取り出し？後々使うかもなので残す
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    user_c.put("member", document.get("name"));
                    member_c.put(document.get("name").toString(), "false");//あとユーザ名入れる
                    name_check = document.get("name").toString();

                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //Cloud Firestore上にデータを格納、グループ名はドキュメント名に
        user_c.put("secret_word", secret_word);
        user_c.put("owner", uid); //ここで必要なもの入れる
        //user_c.put("member", document.get("name"))//あとユーザ名入れる
        //Log.d(TAG, name_check); //これ入れると強制終了する

        db.collection("group").document(group_name)
                .set(user_c)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        showDialog("作成成功");
                        //Intent intent_teamNameDecision = new Intent(getApplication(), Home.class);
                        //startActivity(intent_teamNameDecision);
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error writting document", e);
                        showDialog("作成失敗");
                    }
                });


        //サブコレクションを作成できないから、配列を選択すべき？→できた
        db.collection("group").document(group_name)
                .collection("member").document("member")
                .set(member_c)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        showDialog("作成成功");
                        Intent intent_teamNameDecision = new Intent(getApplication(), Home.class);
                        startActivity(intent_teamNameDecision);
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error writting document", e);
                        showDialog("作成失敗");
                    }
                });

    }

    //確認ダイアログ処理
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
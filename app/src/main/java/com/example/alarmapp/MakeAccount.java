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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

//firebase authentication用
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MakeAccount extends AppCompatActivity {

    private static final String TAG = "DocSnippets";

    //FirebaseAuthのプライベートメンバ変数
    private FirebaseAuth mAuth;

    //Cloud Firestoreのプライベートメンバ変数
    private FirebaseFirestore db;


    //データベースへのアクセス用の変数
    Map<String, Object> user_c = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);
        //ID, パスワードをデータベースに渡す
        //インスタンスの初期化
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button nameDecision_button = findViewById(R.id.nameDecision_button);
        nameDecision_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((TextView)findViewById(R.id.name_editText)).getText().toString();
                String email = ((TextView)findViewById(R.id.newId_editText)).getText().toString();
                String password = ((TextView)findViewById(R.id.newPassword_editText)).getText().toString();
                createUser(name, email, password);

                Intent intent_nameDecision = new Intent(getApplication(),Title.class);
                startActivity(intent_nameDecision);
            }
        });
    }

    private void createUser(String name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    showDialog(user.getUid());
                }
            }
        });

        //Cloud Firestore上にデータを格納
        user_c.put("name", name);
        user_c.put("email", email); //ここで必要なもの入れる
        user_c.put("password", password);
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users").document(user.getUid())
                .set(user_c)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writting document", e);
                    }
                });

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
package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignIn extends AppCompatActivity {

    //画面表示の呼び出し(全ファイル必須)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin); //ここを対応するxmlファイルに変更


        //ID, パスワードをデータベースに渡す


        //SignIn_buttonが押された時用の画面遷移関数
        //データベースへ渡す関数の実行
        //Title.javaを呼び出す？(画面遷移)
        Button signIn_button = findViewById(R.id.signIn_button);
        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_signIn = new Intent(getApplication(), Title.class);
                startActivity(intent_signIn);
            }
        });


        //MakeAccount_buttonが押された時用の画面遷移関数
        //MakeAccount.javaを呼び出す？(画面遷移)
        Button makeAccount_button = findViewById(R.id.makeAccount_button);
        makeAccount_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_makeAccount = new Intent(getApplication(), MakeAccount.class);
                startActivity(intent_makeAccount);
            }
        });

    }

}
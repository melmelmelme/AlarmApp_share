package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class SignIn extends AppCompatActivity {

    //画面表示の呼び出し(全ファイル必須)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin); //ここを対応するxmlファイルに変更
    }

    //ID, パスワードをデータベースに渡す



    //SignIn_buttonが押された時用の画面遷移関数
    //データベースへ渡す関数の実行
    //Title.javaを呼び出す？(画面遷移)



    //MakeAccount_buttonが押された時用の画面遷移関数
    //MakeAccount.javaを呼び出す？(画面遷移)
    public void click_makeAccount_button(View view){
        Intent intent = new Intent(SignIn.this, MakeAccount.class);
        startActivity(intent);
    }


}
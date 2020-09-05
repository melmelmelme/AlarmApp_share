package com.example.alarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//firebase authentication用
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    public boolean flag = false;

    //FirebaseAuthのプライベートメンバ変数
    private FirebaseAuth mAuth;

    //画面表示の呼び出し(全ファイル必須)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin); //ここを対応するxmlファイルに変更
        //fragの初期化
        flag = false;

        //ID, パスワードをデータベースに渡す

        mAuth = FirebaseAuth.getInstance();

        //SignIn_buttonが押された時用の画面遷移関数
        //データベースへ渡す関数の実行
        //Title.javaを呼び出す？(画面遷移)→画面遷移はsignInメソッドへ移動(9/6)
        Button signIn_button = findViewById(R.id.signIn_button);
        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ログイン処理
                String email = ((TextView)findViewById(R.id.id_editText)).getText().toString();
                String password = ((TextView)findViewById(R.id.password_editText)).getText().toString();
                signIn(email, password);

                /*if(flag == true) {
                    Intent intent_signIn = new Intent(getApplication(), Title.class);
                    startActivity(intent_signIn);
                }*/
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

    //onclick内に書くと引数の関係でエラーが出る。外に書くとFirebaseAuthインスタンスの初期化がうまくできているか謎
    //ログインの処理メソッド
    //画面遷移もこちらの条件分岐に移動(9/6)
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    flag = true;
                    //FirebaseUser user = mAuth.getCurrentUser();
                    //showDialog(user.getUid());
                    Intent intent_signIn = new Intent(getApplication(), Title.class);
                    startActivity(intent_signIn);
                }else{
                    showDialog("ERROR!：メールアドレスかパスワードが間違っています。");
                }
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
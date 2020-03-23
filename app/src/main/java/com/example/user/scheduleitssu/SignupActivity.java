package com.example.user.scheduleitssu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private static final String TAG="SIGNUP Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signup=(Button) findViewById(R.id.sign_upbtn);
        signup.setOnClickListener(this);

        Button gotologin=(Button) findViewById(R.id.gotologin);
        gotologin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onBackPressed(){ //onBackPressed 공부 필요
        super.onBackPressed();
        //벡버튼 누르면 앱 종료
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }
    @Override
    public void onClick(View v){
      switch(v.getId()){
          case R.id.sign_upbtn:
              Log.e("클릭","클릭");
              signUp();
              break;
          case R.id.gotologin:
             startMyActivity(LoginActivity.class);
      }
    }

    private void signUp() {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String passwordcheck = ((EditText) findViewById(R.id.passwordcheck)).getText().toString();

        if(email.length()>0 && password.length()>0 && passwordcheck.length()>0) {
            if (password.equals(passwordcheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    startToast("성공적으로 회원가입 되었습니다.");
                                    startMyActivity(MainActivity.class);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }
                                    //   Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //          Toast.LENGTH_SHORT).show();
                                    //  updateUI(null);
                                }

                                // ...
                            }
                        });

            } else {
                startToast("비밀번호가 일치하지 않습니다.");


            }
        } else{
            startToast("이메일 또는 비밀번호를 입력해주세요");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void startMyActivity(Class c){
        Intent intent=new Intent(this,c);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        //로그인 후 돌아가기 버튼 누르면 로그인화면으로 돌아가는 것이 아니도록 함
        startActivity(intent);
    }

}
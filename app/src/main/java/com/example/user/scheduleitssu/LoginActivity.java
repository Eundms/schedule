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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private static final String TAG="LOGIN Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signin=(Button) findViewById(R.id.sign_inbtn);
        signin.setOnClickListener(this);

        Button gotopassword=(Button) findViewById(R.id.gotopasswordresetbtn);
        gotopassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v){
      switch(v.getId()){
          case R.id.sign_inbtn:
              Log.e("클릭","클릭");
              signIn();
              break;
          case R.id.gotopasswordresetbtn:
              startMyActivity(PasswordResetActivity.class);
              break;
      }
    }

    private void signIn() {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        if(email.length()>0 && password.length()>0 ) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                startToast("로그인에 성공하였습니다.");
                                startMyActivity(MainActivity.class);
                                FirebaseUser user = mAuth.getCurrentUser();
                              //  updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                startToast("로그인에 실패하였습니다.");
                               // updateUI(null);
                            }

                            // ...
                        }
                    });

        }
        else{
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
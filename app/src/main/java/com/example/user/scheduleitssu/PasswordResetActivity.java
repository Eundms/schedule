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

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private static final String TAG="PASSWORD_RESET Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Button sendbtn=(Button) findViewById(R.id.sendbtn);
        sendbtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v){
      switch(v.getId()){
          case R.id.sendbtn:
              Log.e("클릭","클릭");
              send();
              break;
      }
    }

    private void send() {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();

        if(email.length()>0) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startToast("이메일을 보냈습니다.");
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
        else{
            startToast("이메일을 입력해주세요");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }



}
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

import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.example.user.scheduleitssu.DataClass.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN=900;
    private static final String TAG="LOGIN Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        SignInButton signin=(SignInButton) findViewById(R.id.loginbtn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //   Toast.makeText(LoginActivity.this,"로그인 실ttl",Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately->SHA1
                Toast.makeText(LoginActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"파이어베이스 아이디 생성 실패",Toast.LENGTH_SHORT).show();
                        } else{
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this, CalendarUtil.class);
                            startActivity(intent);
                            /////////////
                            /* */
                            FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference=firebaseDatabase.getReference();
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            String uid=user.getDisplayName()+"_"+user.getUid();
                            HashMap<String,Object>add_userinfo=new HashMap<>();
                            add_userinfo.put("UserInfo",new User(uid,"",""));
                            add_userinfo.put("Uid",uid);
                            databaseReference.child("Student").child(uid).updateChildren(add_userinfo);
                            //add_userinfo.put(uid,new User(uid,"",""));
                            // databaseReference.child("Student").updateChildren(add_userinfo);

                            finish();

                            ///////////////
                            Toast.makeText(LoginActivity.this,"파이어베이스 아이디 생성 완료",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this,"파이어베이스 아이디 생성dddd",Toast.LENGTH_SHORT).show();
    }


    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
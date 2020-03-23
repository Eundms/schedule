package com.example.user.scheduleitssu;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backbtn=(ImageView)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.settings_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
                case R.id.settings_information:
                    Toast.makeText(getApplicationContext(), "settings_information", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this,InformationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.settings_payment:
                    Toast.makeText(getApplicationContext(), "settings_payment", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings_alarm:
                    Toast.makeText(getApplicationContext(), "settings_alarm", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings_friends:
                    Toast.makeText(getApplicationContext(), "settings_friends", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings_logout:
                    Toast.makeText(getApplicationContext(), "settings_logout", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startSignUpActivity();
                            //로그아웃 하면 회원가입 화면으로 돌아감
                    break;
                default:
                    return false;
        }
        return true;
    }

    private void startSignUpActivity(){
        Intent intent=new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
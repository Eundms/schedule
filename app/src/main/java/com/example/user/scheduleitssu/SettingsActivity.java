package com.example.user.scheduleitssu;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageView backbtn;

    private FirebaseAuth mAuth;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
        View nav_header_view = navigationView.getHeaderView(0);
        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.username);
        nav_header_id_text.setText(user.getDisplayName());

        TextView email=(TextView)nav_header_view.findViewById(R.id.useremail);
        email.setText(user.getEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
                case R.id.settings_information:
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
                    Intent i = new Intent(this, LoginActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                   //로그아웃 하면 회원가입 화면으로 돌아감
                    break;
                    default:
                    return false;
        }
        return true;
    }



}
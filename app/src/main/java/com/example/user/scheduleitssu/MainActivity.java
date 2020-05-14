package com.example.user.scheduleitssu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("SCHEDULEITSSU",MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("initial",false))
            initialSetting();
        if(sharedPreferences.getBoolean("notification",true))
            startNotificationListener();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("UNIVERSITY NOTE");
        toolbar.setTitleMarginStart(170);
        setSupportActionBar(toolbar);

        //TabLayout과 ViewPager 초기화
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        viewPager=(ViewPager)findViewById(R.id.viewpager);

        //PageAdpater 추가
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                //    Log.d("TAB_POSITION",""+tab.getPosition());

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        toolbar.setLogo(R.drawable.notelist_icon);

    }

    protected void initialSetting(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String path = user.getDisplayName()+"_"+user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Student").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    HashMap<String, Object> initial = new HashMap<>();
                    initial.put("Uid",path);
                    FirebaseDatabase.getInstance().getReference().child("Student").child(path).updateChildren(initial);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("initial",true); //intial이 되었다고 sharedpreferences에 정보값 변경
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void startNotificationListener(){
        boolean startByNoti = getIntent().getBooleanExtra("Started By Notification",false);
        if(!startByNoti) {

        }
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    //toolbar에 삽입된 메뉴 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_icon:
                Toast.makeText(getApplicationContext(), "검색 버튼이 클릭됨", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings_icon:
                //Toast.makeText(getApplicationContext(), "설정 버튼이 클릭됨", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }


    private void startSignINActivity(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
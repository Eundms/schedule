package com.example.user.scheduleitssu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //유저가 로그인이 안된 상태면 회원가입/로그인 창으로 돌아감
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startSignUpActivity();
            Log.d("유저 없음","유저");
        } else{
            //회원가입 or 로그인
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    String name = profile.getDisplayName();
                }
            }
        }


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
        //앱의 로고 설정
        toolbar.setLogo(R.drawable.notelist_icon);

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

    private void startSignUpActivity(){
        Intent intent=new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}
package com.example.user.scheduleitssu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener{
    TextView nexttimeaddinfo;
    Button addinfobtn;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this.getApplicationContext();
        setContentView(R.layout.activity_information);

        nexttimeaddinfo=(TextView)findViewById(R.id.다음에입력);
        addinfobtn=(Button)findViewById(R.id.addinformationbtn);
        //이름은 default로 앞 메일계정에서 받아야한다. 하지만, 고칠 수 있게!
        /*Intent intent = getIntent(); intent.getParcelableExtra("editTrade");*/
        nexttimeaddinfo.setOnClickListener(this);
        addinfobtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.다음에입력:
            finish();
                break;
            case R.id.addinformationbtn:
                finish();
            //파이어베이스에 추가하는 함수 호출
                break;
        }
    }
}


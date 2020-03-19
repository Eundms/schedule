package com.example.user.scheduleitssu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddClassActivity extends AppCompatActivity {
Button backbtn;
Button addbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        backbtn=(Button) findViewById(R.id.addsubjectbackbtn);
        addbtn=(Button) findViewById(R.id.addsubjectbtn) ;
        backbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "취소하기 클릭됨", Toast.LENGTH_LONG).show();
                finish();

            }
        });
        addbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "등록하기 클릭됨", Toast.LENGTH_LONG).show();
                //파이어베이스에 등록하는코드 필요
            }
        });

    }
}

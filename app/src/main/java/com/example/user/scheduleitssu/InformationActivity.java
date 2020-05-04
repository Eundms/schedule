package com.example.user.scheduleitssu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener{
    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;

    TextView nexttimeaddinfo;
    Button addinfobtn;
    Context context;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this.getApplicationContext();
        setContentView(R.layout.activity_information);

        nexttimeaddinfo=(TextView)findViewById(R.id.다음에입력);
        addinfobtn=(Button)findViewById(R.id.addinformationbtn);
        imageView=(ImageView)findViewById(R.id.edit_userimag);

        //이름은 default로 앞 메일계정에서 받아야한다. 하지만, 고칠 수 있게!
        /*Intent intent = getIntent(); intent.getParcelableExtra("editTrade");*/
        nexttimeaddinfo.setOnClickListener(this);
        addinfobtn.setOnClickListener(this);
        imageView.setOnClickListener(this);
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
            case R.id.edit_userimag:
                loadImagefromGallery(imageView);
                break;
        }
    }
    /////////////////////////////////////////////////////////////////////////////
    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                int nh = (int) (bitmap.getHeight() * (1200 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1200, nh, true);

                imgView = (ImageView) findViewById(R.id.image);
                imgView.setImageBitmap(scaled);
                imgView.setBackground(new ShapeDrawable(new OvalShape()));

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
    /////////////////////////////////////////////////////////////////////////////

}


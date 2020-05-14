package com.example.user.scheduleitssu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.example.user.scheduleitssu.DataClass.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener{
    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    Context context;
    ImageView editimagebtn;
    EditText nickname;
    EditText contactinfo;
    /*개인 정보 "등록 및 수정, 취소" 버튼*/
    TextView nexttimeaddinfo;
    Button addinfobtn;
    /*firebase*/
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String uid=user.getDisplayName()+"_"+user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this.getApplicationContext();
        setContentView(R.layout.activity_information);

        nexttimeaddinfo=(TextView)findViewById(R.id.다음에입력);
        addinfobtn=(Button)findViewById(R.id.addinformationbtn);
        editimagebtn =(ImageView)findViewById(R.id.edit_userimag);
        //이름은 default로 앞 메일계정에서 받아야한다. 하지만, 고칠 수 있게!
        nexttimeaddinfo.setOnClickListener(this);
        addinfobtn.setOnClickListener(this);
        editimagebtn.setOnClickListener(this);

        nickname=(EditText)findViewById(R.id.editNicName);
        contactinfo=(EditText)findViewById(R.id.editContectInfo);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.다음에입력:
            finish();
                break;
            case R.id.addinformationbtn:
                //파이어베이스에 닉네임, 연락처 추가하는 함수 호출
                HashMap<String,Object> add_userinfo=new HashMap<>();
                add_userinfo.put("UserInfo",new User(uid,nickname.getText().toString(),contactinfo.getText().toString()));
                myRef.child("Student").child(uid).updateChildren(add_userinfo);
                //Log.d("KEY",myRef.child("Student").child(uid).child("UserInfo").getKey());
                finish();
                break;
            case R.id.edit_userimag:
                loadImagefromGallery(editimagebtn);
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


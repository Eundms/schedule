package com.example.user.scheduleitssu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.scheduleitssu.DataClass.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//firebase database





public class AddClassActivity extends AppCompatActivity implements View.OnClickListener{
FirebaseCommunicator firebaseCommunicator=new FirebaseCommunicator();
Button backbtn;
Button addbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        backbtn=(Button) findViewById(R.id.addsubjectbackbtn);
        addbtn=(Button) findViewById(R.id.addsubjectbtn) ;

        backbtn.setOnClickListener(this);
        addbtn.setOnClickListener(this);



    }

    //firebase를 위한 객체 생성?
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addsubjectbtn:
                TextView groupname=(TextView)findViewById(R.id.makegroupname);
                String group=groupname.getText().toString();
                Subject sub=new Subject(group);
                String uid=user.getUid();
                //subject class 업로드
                /*databaseReference.child("Student").child(uid).child("Subject").setValue(sub);
                databaseReference.child("Subject").push().setValue(sub);*/



                finish();
                break;
            case R.id.addsubjectbackbtn:
                //Toast.makeText(getApplicationContext(), "취소하기 클릭됨", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

    }
}

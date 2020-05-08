package com.example.user.scheduleitssu;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TextView subjectname=(TextView)findViewById(R.id.makesubjectname);
                      /////////////////if you want to add code directly, do it!!!!!!////////////////
                      /*  ArrayList<Note> notes=new ArrayList<Note>();
                        notes.add(new Note("{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\" +
                                "u003e\\u003cu\\u003etest_note1\\u003c/u\\u003e\\u003c/p\\u003e\\n\"]," +
                                "\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}"));
                        notes.add(new Note("{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\" +
                                "u003e\\u003cu\\u003eserialized2\\u003c/u\\u003e\\u003c/p\\u003e\\n\"]," +
                                "\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}"));
                        Subject sub=new Subject(subjectname.getText().toString(),notes);*/
                        /////////////////////////////////////////////////////////////////////////////
                        /*1번째 버전*/
                        Subject sub=new Subject(subjectname.getText().toString(),new ArrayList<Note>());
                        String uid=user.getDisplayName()+"_"+user.getUid();
                        HashMap<String,Object>add_subject=new HashMap<>();
                        add_subject.put("Subject_"+sub.getClassname(),sub);
                        databaseReference.child("Student").child(uid).child("Subject").updateChildren(add_subject);

                        /*2번째 버전
                        Subject sub=new Subject(subjectname.getText().toString(),new ArrayList<Note>());
                        String uid=user.getDisplayName()+"_"+user.getUid();
                        HashMap<String, Object> add_subject = new HashMap<>();
                        add_subject.put("Subject_"+sub.getClassname(),sub);
                        databaseReference.child("Subject").updateChildren(add_subject);

                        HashMap<String,Object>add_subject_id=new HashMap<>();
                        add_subject_id.put("Subject_"+sub.getClassname(),"Subject_"+sub.getClassname());
                        databaseReference.child("Student").child(uid).child("SubjectID").updateChildren(add_subject_id);*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //subject class 업로드
               // FirebaseDatabase.getInstance().getReference().child("Student").child(path).updateChildren(initial);

                finish();
                break;
            case R.id.addsubjectbackbtn:
                finish();
                break;
        }

    }
}

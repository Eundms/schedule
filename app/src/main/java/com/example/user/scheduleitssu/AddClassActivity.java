package com.example.user.scheduleitssu;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//firebase database





public class AddClassActivity extends AppCompatActivity implements View.OnClickListener{
Button backbtn;
Button addbtn;

String subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        backbtn= findViewById(R.id.addsubjectbackbtn);
        addbtn= findViewById(R.id.addsubjectbtn);

        backbtn.setOnClickListener(this);
        addbtn.setOnClickListener(this);



    }

    //firebase를 위한 객체 생성
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
                        TextView subjectname= findViewById(R.id.makesubjectname);
                        TextView subjectinfo= findViewById(R.id.makesubjectinfo);

                        if(subjectname.getText().length()<=0){
                            Toast.makeText(AddClassActivity.this, "subject 이름을 입력하세요.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Subject sub = new Subject(subjectname.getText().toString(), subjectinfo.getText().toString(), new ArrayList<Note>());
                            subject = subjectname.getText().toString();
                            String uid = user.getDisplayName() + "_" + user.getUid();
                            HashMap<String, Object> add_subject = new HashMap<>();
                            add_subject.put("Subject_" + sub.getClassname(), sub);
                            databaseReference.child("Student").child(uid).child("SubjectList").updateChildren(add_subject);
                            new CalendarUtil().execute();
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


              break;

            case R.id.addsubjectbackbtn:
                finish();
                break;
        }

    }

    class CalendarUtil extends AsyncTask<Void, Void, String> {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        GoogleAccountCredential credential;
        com.google.api.services.calendar.Calendar service = null;

        public CalendarUtil(){


            // Google Accounts
            credential =
                    GoogleAccountCredential.usingOAuth2(getApplicationContext(), Collections.singleton(CalendarScopes.CALENDAR));
            credential.setSelectedAccountName(user.getEmail());

            // Tasks client
            service =
                    new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                            .setApplicationName("Uninote").build();

        }


        @Override
        protected String doInBackground(Void ...voids) {

            // Create a new calendar
            com.google.api.services.calendar.model.Calendar calendar = new Calendar();
            calendar.setSummary(subject);
            calendar.setTimeZone("Asia/Seoul");

            // 구글 캘린더에 새로 만든 캘린더를 추가
            Calendar createdCalendar = null;
            try {
                createdCalendar = service.calendars().insert(calendar).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 추가한 캘린더의 ID를 가져옴.
            String calendarId = createdCalendar.getId();
            return null;
        }
    }

}

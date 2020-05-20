package com.example.user.scheduleitssu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.scheduleitssu.DataClass.Group;
import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.github.irshulx.models.EditorContent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectActivity extends AppCompatActivity  implements NoteAdapter.OnNoteItemClickListener{
    Subject subject;
    Toolbar toolbar;
    TextView subject_default;
    RecyclerView NoteRecyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> noteArrayList;
    private static final int SUBJECTACTIVITY_REQUEST=22;
    /*firebase 을 위해서*/
    DatabaseReference myRef;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userid=user.getDisplayName()+"_"+user.getUid();
@Override
protected void onResume() {
    super.onResume();
    myRef=database.getReference().child("Student").child(userid).child("SubjectList").child("Subject_"+subject.getClassname()).child("notelist");
    myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        noteArrayList.clear();
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) { //하위노드가 없을 떄까지 반복
                                            Note note= dataSnapshot2.getValue(Note.class);
                                            noteArrayList.add(note);
                                        }
                                        noteAdapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
        if(noteArrayList.size()==0){ subject_default.setVisibility(View.VISIBLE); }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        toolbar = (Toolbar) findViewById(R.id.subject_toolbar);
        subject_default=(TextView)findViewById(R.id.subject_default);

        Intent receive=getIntent();
        noteArrayList=new ArrayList<>();
        processIntent(receive);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NoteRecyclerView=(RecyclerView)findViewById(R.id.subject_noterecyclerview);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        NoteRecyclerView.setLayoutManager(layoutManager2);
        noteAdapter=new NoteAdapter(this,noteArrayList);
        noteAdapter.setOnNoteItemClickListener(this);
        NoteRecyclerView.setAdapter(noteAdapter);
    }
    private void processIntent(Intent receive){
        if(receive.getStringExtra("EXIST").equals("EXIST")&&receive.getStringExtra("DATATYPE").equals("SUBJECT")) {

            String infotype=receive.getStringExtra("INFOTYPE");
                if (infotype.equals("SUBJECTINFO_DEFAULT")) {/*notelist없음*/
                subject = receive.getParcelableExtra("DATA");
                toolbar.setTitle(subject.getClassname());
                subject_default.setVisibility(View.VISIBLE);
                } else if (infotype.equals("SUBJECTINFO_NOTE")) {/*notelist있음*/
                subject = receive.getParcelableExtra("DATA");
                toolbar.setTitle(subject.getClassname());
                noteArrayList = subject.getNotelist();
                } else {
                finish();
                }
            }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
            case R.id.addnote_add_menu:{
                Intent intent = new Intent(this, EditNoteActivity.class);
                intent.putExtra("EXIST","EXIST");
                intent.putExtra("DATATYPE","SUBJECT");
                intent.putExtra("INFOTYPE","NOTEINFO_DEFAULT");
                intent.putExtra("DATA",subject);

                startActivityForResult(intent,SUBJECTACTIVITY_REQUEST);
                return true;
            }
            case R.id.deletesubject_delete_menu:{
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SubjectActivity.this);
                alert_confirm.setMessage("과목을 삭제 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new CalendarUtil().execute();

                                // 'YES'
                                //Initialize Calendar service with valid OAuth credentials
                                //Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials).setApplicationName("applicationName").build();
                                // Delete an event
                                //service.calendars().delete(subject.getClassname()).execute();
                                database.getReference().child("Student").child(userid).child("SubjectList").child("Subject_"+subject.getClassname()).removeValue();
                                finish();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();


                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUBJECTACTIVITY_REQUEST && resultCode == RESULT_OK){
            String notecontent=data.getStringExtra("NOTECONTENT");
            String notetitle=data.getStringExtra("NOTETITLE");
            String noteeventid=data.getStringExtra("NOTEEVENTID");
            Log.d("NOTE2",noteeventid);

            Note newnote=new Note(notecontent,notetitle);
            newnote.setEventId(noteeventid);
            Log.d("NOTE2",newnote.getEventId());
            /*만든시간 추가*/
            SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
            newnote.setWhenmake_note(format1.format(new Date()));

            noteArrayList.add(newnote);
/*노트 리스트를 올리는 내용*///////////////////////////////////////////////////////////////////////////////////////////
            myRef=database.getReference().child("Student").child(userid).child("SubjectList").child("Subject_"+subject.getClassname());
            Map<String,Object>newnotes=new HashMap<>();
            newnotes.put("notelist",noteArrayList);
            myRef.updateChildren(newnotes);
            noteAdapter.notifyDataSetChanged();
            subject_default.setVisibility(View.GONE);
        }else if(requestCode == SUBJECTACTIVITY_REQUEST && resultCode == RESULT_CANCELED){
        }else{
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_addnote, menu);
        return true;
    }



    @Override
    public void onNoteItemClick(View v, int pos) {
        //Log.d("NOTE","Clicked");
        Intent intent=new Intent(this,DetailNoteActivity.class);
        intent.putExtra("EXIST","EXIST");
        intent.putExtra("DATATYPE","SUBJECT");
        intent.putExtra("INFOTYPE","SUBJECTINFO_CONTENT");
        intent.putExtra("POSITION",Integer.toString(pos));
        intent.putExtra("DATA",subject);
        startActivity(intent);
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



            try {
                service.calendars().delete(getCalendarID(subject.getClassname())).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        /*
         * 캘린더 이름에 대응하는 캘린더 ID를 리턴
         */
        private String getCalendarID(String calendarTitle){

            String id = null;

            // Iterate through entries in calendar list
            String pageToken = null;
            do {
                CalendarList calendarList = null;
                try {
                    calendarList = service.calendarList().list().setPageToken(pageToken).execute();
                } catch (UserRecoverableAuthIOException e) {
                    startActivityForResult(e.getIntent(), 1001);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                List<CalendarListEntry> items = calendarList.getItems();


                for (CalendarListEntry calendarListEntry : items) {

                    if ( calendarListEntry.getSummary().toString().equals(calendarTitle)) {

                        id = calendarListEntry.getId().toString();
                    }
                }
                pageToken = calendarList.getNextPageToken();
            } while (pageToken != null);

            return id;
        }

    }



}

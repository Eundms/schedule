package com.example.user.scheduleitssu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorContent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.api.services.calendar.Calendar;


public class DetailNoteActivity extends AppCompatActivity {
    private static final int DETAILNOTEACTIVITY_REQUEST = 11;
    //////////////////////////////////////////////////////////////////
    /*firebase 을 위해서*/

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userid=user.getDisplayName()+"_"+user.getUid();
    DatabaseReference myRef=database.getReference().child("Student").child(userid).child("SubjectList");

    //////////////////////////////////////////////////////////////////
    Subject subject;
    Note note;
    String serialized;
    Editor renderer;
    int position;
    Toolbar toolbar;
    TextView notemakedate;
    TextView noteeditdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        processIntent();
        notemakedate=findViewById(R.id.note_make_date);
        noteeditdate=findViewById(R.id.note_edit_date);
        notemakedate.setText(notemakedate.getText()+note.getWhenmake_note());
        noteeditdate.setText(noteeditdate.getText()+note.getEditdate_note());
        toolbar = (Toolbar) findViewById(R.id.detailnote_toolbar);
        if(note.getTitle()==null){toolbar.setTitle(""+(position+1)+"번째 노트");}
        else{toolbar.setTitle(note.getTitle());}
        setnote();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    void processIntent(){
        Intent getIntent=getIntent();
        if(getIntent.getStringExtra("EXIST").equals("EXIST")&&getIntent.getStringExtra("DATATYPE").equals("SUBJECT")){
            if(getIntent.getStringExtra("INFOTYPE").equals("SUBJECTINFO_CONTENT")){
            subject= getIntent.getParcelableExtra("DATA");
            position=Integer.parseInt(getIntent.getStringExtra("POSITION"));
            note=subject.getNotelist().get(position);
            serialized= note.getContent();
            }else{
            /*노트가 없으면 클릭 자체가 안되므로 else에 오는 경우는 없음*/
            }
        }
    }
void setnote(){
    renderer= (Editor)findViewById(R.id.renderer);
    String content= serialized;
    EditorContent Deserialized= renderer.getContentDeserialized(content);
    renderer.render(Deserialized);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAILNOTEACTIVITY_REQUEST && resultCode == RESULT_OK){
            String serialized= data.getStringExtra("NOTECONTENT");
            //Log.d("DETAILNOTEACTIVITY","Success: "+serialized);
            String notetitle=data.getStringExtra("NOTETITLE");
            toolbar.setTitle(notetitle);
            note.setTitle(notetitle);

            note.setContent(serialized);
            this.serialized=serialized;
            String content= serialized;
            EditorContent Deserialized2= renderer.getContentDeserialized(content);
            renderer.render(Deserialized2);

//////////////////////////////////////////////////////////////////
            /*firebase 함수수*/
            SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
            note.setEditdate_note(format1.format(new Date()));
            Map<String,Object>editnote=new HashMap<>();
            editnote.put(Integer.toString(position),note);
            myRef.child("Subject_"+subject.getClassname()).child("notelist").updateChildren(editnote);
//////////////////////////////////////////////////////////////////

        }else if(requestCode == DETAILNOTEACTIVITY_REQUEST && resultCode == RESULT_CANCELED){
        }else{
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_detailnote, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
            case R.id.detailnote_edit_menu:{
                Intent intent = new Intent(this, EditNoteActivity.class);
                intent.putExtra("EXIST","EXIST");
                intent.putExtra("DATATYPE","SUBJECT");
                intent.putExtra("INFOTYPE","SUBJECTINFO_CONTENT");
                intent.putExtra("POSITION",Integer.toString(position));
                intent.putExtra("DATA",subject);
                setResult(RESULT_OK, intent);

                startActivityForResult(intent,DETAILNOTEACTIVITY_REQUEST);
                return true;
            }
            case R.id.detailnote_delete_menu:{
                //삭제하는 기능(firebase) NOTELIST를 아제 올리고 내려야 함.
                /*delete note하는 코드를 옮겨야함  */

                new CalendarUtil(note.getEventId()).execute();
                subject.getNotelist().remove(position);
                Map<String,Object>deletenote=new HashMap<>();
                deletenote.put("notelist",subject.getNotelist());
                myRef.child("Subject_"+subject.getClassname()).updateChildren(deletenote);
/*
//Initialize Calendar service with valid OAuth credentials
                Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials).setApplicationName("applicationName").build();
// Delete an event
                service.events().delete('primary', "eventId").execute();
*/
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
   /* public Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/GreycliffCF-Bold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/GreycliffCF-Bold.ttf");
        return typefaceMap;
    }
    public Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Lato-Medium.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Lato-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Lato-MediumItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Lato-BoldItalic.ttf");
        return typefaceMap;
    }*/


    class CalendarUtil extends AsyncTask<Void, Void, String> {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        GoogleAccountCredential credential;
        com.google.api.services.calendar.Calendar service = null;
        String eventID;
        public CalendarUtil(String eventID){

            this.eventID=eventID;

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
                service.events().delete(getCalendarID(subject.getClassname()), eventID).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

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





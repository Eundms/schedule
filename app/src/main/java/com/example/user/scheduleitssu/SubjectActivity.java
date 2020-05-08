package com.example.user.scheduleitssu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.InternalTokenProvider;

import java.util.ArrayList;
import java.util.HashMap;
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

            String infotype=receive.getStringExtra("SUBJECTINFOTYPE");
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
                intent.putExtra("EXIST","NO");

                startActivityForResult(intent,SUBJECTACTIVITY_REQUEST);
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
            Note newnote=new Note(notecontent);
            noteArrayList.add(newnote);
/*노트 리스트를 올리는 내용*///////////////////////////////////////////////////////////////////////////////////////////
            myRef=database.getReference().child("Student").child(userid).child("Subject").child("Subject_"+subject.getClassname());
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
        intent.putExtra("SUBJECTINFOTYPE","SUBJECTINFO_CONTENT");
        intent.putExtra("POSITION",Integer.toString(pos));
        intent.putExtra("DATA",subject);
        startActivity(intent);
    }

}

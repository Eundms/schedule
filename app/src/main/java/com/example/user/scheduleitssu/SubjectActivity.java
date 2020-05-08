package com.example.user.scheduleitssu;

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
import android.widget.Toast;

import com.example.user.scheduleitssu.DataClass.Group;
import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.google.firebase.internal.InternalTokenProvider;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity  implements NoteAdapter.OnNoteItemClickListener{
    Subject subject;
    Toolbar toolbar;

    RecyclerView NoteRecyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> noteArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        toolbar = (Toolbar) findViewById(R.id.subject_toolbar);

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
                /*PLEASE ADD NOTE*/
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

                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
        intent.putExtra("DATATYPE","NOTE");
        intent.putExtra("NOTEINFOTYPE","NOTEINFO_CONTENT");
        intent.putExtra("DATA",noteArrayList.get(pos));
        startActivity(intent);
    }

}

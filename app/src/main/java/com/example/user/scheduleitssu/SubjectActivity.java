package com.example.user.scheduleitssu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        processIntent(receive);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        noteArrayList=new ArrayList<>();
        noteArrayList.add(new Note("Day1_과제1보충설명","","","",""));
        noteArrayList.add(new Note("Day2_과제2보충설명","","","",""));
        noteArrayList.add(new Note("Day3_과제3보충설명","","","",""));




        NoteRecyclerView=(RecyclerView)findViewById(R.id.subject_noterecyclerview);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        NoteRecyclerView.setLayoutManager(layoutManager2);

        // Log.d("group",group.getGroupname()+group.getNumofmember());



        noteAdapter=new NoteAdapter(this,noteArrayList);
        noteAdapter.setOnNoteItemClickListener(this);
        NoteRecyclerView.setAdapter(noteAdapter);
    }
    private void processIntent(Intent receive){
        String infotype= receive.getStringExtra("SubjectInfoType");
        Log.d("infotype",infotype);
        if(infotype.equals("SUBJECTINFO_DEFAULT")){//string이랑 같냐
            subject=receive.getParcelableExtra("subject_info_data");
            toolbar.setTitle(subject.getClassname());

        }else if(infotype.equals("SUBJECTINFO_NONOTE")){
            subject=receive.getParcelableExtra("subject_info_data");
            toolbar.setTitle(subject.getClassname());
            //노트 부분에 노트를 추가해주세요라고 나오게 레이아웃 설정
        }else if(infotype.equals("SUBJECTINFO_NOGROUP")){
            subject=receive.getParcelableExtra("subject_info_data");
            toolbar.setTitle(subject.getClassname());
            //그룹 부분에 그룹를 추가해주세요라고 나오게 레이아웃 설정

        }else{
           finish();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //맞는지 모르겠음
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onNoteItemClick(View v, int pos) {
        Intent intent=new Intent(this,DetailNoteActivity.class);
        intent.putExtra("NoteInfoType","NOTEINFO_DEFAULT");
        intent.putExtra("note_info_data",noteArrayList);
        startActivity(intent);
    }
}

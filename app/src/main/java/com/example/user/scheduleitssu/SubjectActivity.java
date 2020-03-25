package com.example.user.scheduleitssu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.scheduleitssu.DataClass.Group;
import com.example.user.scheduleitssu.DataClass.Subject;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity  implements ProjectAdapter.OnItemClickListener{
    Subject subject;
    Toolbar toolbar;
    RecyclerView ProjectRecyclerView;
    ProjectAdapter projectAdapter;
    ArrayList<Group> projectArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        toolbar = (Toolbar) findViewById(R.id.subject_toolbar);
        Intent receive=getIntent();
        processIntent(receive);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectArrayList=new ArrayList<>();
        projectArrayList.add(new Group("달려라하니팀",3));

        ProjectRecyclerView=(RecyclerView) findViewById(R.id.project_recyclerview);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        ProjectRecyclerView.setLayoutManager(layoutManager);

       // Log.d("group",group.getGroupname()+group.getNumofmember());


        //projectArrayList.add();

        projectAdapter=new ProjectAdapter(this,projectArrayList);
        projectAdapter.setOnItemClickListener(this);
        ProjectRecyclerView.setAdapter(projectAdapter);

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
    public void onItemClick(View v, int pos) {
        Toast.makeText(this, "projectrecyclerview"+pos, Toast.LENGTH_LONG).show();

    }
}

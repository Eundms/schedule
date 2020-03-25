package com.example.user.scheduleitssu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.DataClass.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TabFragment2 extends Fragment implements SubjectAdapter.OnItemClickListener{
    public static final int REQUEST_CODE_ADDCLASS =101;
    public static final int REQUEST_CODE_SUBJECTACTIVITY=102;
    Context context;
    RecyclerView subjectRecyclerView;
    SubjectAdapter subjectAdapter;
    ArrayList<Subject> subjectArrayList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View root=inflater.inflate(R.layout.fragment_tab2,container,false);
        this.context=root.getContext();

        subjectRecyclerView=root.findViewById(R.id.subjectRecyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this.getContext(),2);
        subjectRecyclerView.setLayoutManager(layoutManager);


        subjectArrayList=new ArrayList<>();
        ArrayList<String>notelist=new ArrayList<>();
        notelist.add("day1 note 우와와와와옹 무슨 말이야");//파이어베이스 연결되면, 파이어베이스에서 notelist를 가리키는 아이디값이 들어가야한다
        ArrayList<String>groups=new ArrayList<>();
        groups.add("agroup");//파이어베이스 연결되면, 파이어베이스에서 grouplist를 가리키는 아이디값이 들어가야한다
        subjectArrayList.add(new Subject(groups,"소프트웨어공학(arraylist)","온라인수업",notelist));//SUBJECTINFO_DEFAULT
        subjectArrayList.add(new Subject(groups,"안드로이드(arraylist)","월,수 3:30~",notelist));//SUBJECTINFO_DEFAULT
        subjectArrayList.add(new Subject(groups,"안드로이드(arraylist)","월,수 4:30~",notelist));//SUBJECTINFO_DEFAULT
        subjectArrayList.add(new Subject("데이터베이스(arraylist)","월,수 1:30~",notelist));//SUBJECTINFO_NOGROUP
        subjectArrayList.add(new Subject(groups,"소프트웨어프로젝트(arraylist)","월,수 2:30~"));//SUBJECTINFO_NONOTE




        subjectAdapter=new SubjectAdapter(this.getContext(),subjectArrayList);
        subjectAdapter.setOnItemClickListener(this);
        subjectRecyclerView.setAdapter(subjectAdapter);


        FloatingActionButton addclassbtn=(FloatingActionButton)root.findViewById(R.id.floatingActionButton);
        addclassbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), AddClassActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADDCLASS);
            }
        });


        return root;
    }


    @Override
    public void onItemClick(View v, int pos) {
            Intent intent=new Intent(getContext(),SubjectActivity.class);
            if(subjectArrayList.get(pos).getGroup()==null){
                intent.putExtra("SubjectInfoType","SUBJECTINFO_NOGROUP");
            }else if(subjectArrayList.get(pos).getNotelist()==null){
                intent.putExtra("SubjectInfoType","SUBJECTINFO_NONOTE");
            }else{
                intent.putExtra("SubjectInfoType","SUBJECTINFO_DEFAULT");
            }
            intent.putExtra("subject_info_data",subjectArrayList.get(pos));
            //Toast.makeText(getContext(), "recy"+subjectArrayList.get(pos).getClassname()+subjectArrayList.get(pos).getClasshours(), Toast.LENGTH_LONG).show();
            startActivityForResult(intent,REQUEST_CODE_SUBJECTACTIVITY);
    }
}

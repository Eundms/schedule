package com.example.user.scheduleitssu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.DataClass.Group;

import java.util.ArrayList;

public class TabFragment3 extends Fragment implements ProjectAdapter.OnItemClickListener{
    Context context;
    RecyclerView ProjectRecyclerView;
    ProjectAdapter projectAdapter;
    ArrayList<Group> projectArrayList;//group을 가리키는 데이터 베이스 string형이 들어가야함

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View root=inflater.inflate(R.layout.fragment_tab3,container,false);
        this.context=root.getContext();
        projectArrayList=new ArrayList<>();
        projectArrayList.add(new Group("달려라하니팀",3));

        ProjectRecyclerView=(RecyclerView)root.findViewById(R.id.subject_projectrecyclerview);
        GridLayoutManager layoutManager=new GridLayoutManager(context,2);

        ProjectRecyclerView.setLayoutManager(layoutManager);

        projectAdapter=new ProjectAdapter(context,projectArrayList);
        projectAdapter.setOnItemClickListener(this);
        ProjectRecyclerView.setAdapter(projectAdapter);
        return root;
    }
    @Override
    public void onItemClick(View v, int pos) {
        Toast.makeText(context, "projectrecyclerview"+pos, Toast.LENGTH_LONG).show();

    }
}

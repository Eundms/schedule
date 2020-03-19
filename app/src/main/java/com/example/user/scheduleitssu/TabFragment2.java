package com.example.user.scheduleitssu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.utilClass.Subject;
import com.example.user.scheduleitssu.utilClass.Today2do;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TabFragment2 extends Fragment {
    Context context;
    RecyclerView subjectRecyclerView;
    SubjectAdapter subjectAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View root=inflater.inflate(R.layout.fragment_tab2,container,false);
        this.context=root.getContext();

        subjectRecyclerView=root.findViewById(R.id.subjectRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        subjectRecyclerView.setLayoutManager(layoutManager);
        ArrayList<Subject>subjectArrayList=new ArrayList<>();

        subjectArrayList.add(new Subject("소프트웨어공학(arraylist)","온라인수업"));
        subjectAdapter=new SubjectAdapter(this.getContext(),subjectArrayList);
        subjectRecyclerView.setAdapter(subjectAdapter);


        FloatingActionButton addclassbtn=(FloatingActionButton)root.findViewById(R.id.floatingActionButton);
        addclassbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), AddClassActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "floating btn clicked!", Toast.LENGTH_LONG).show();
            }
        });


        return root;
    }

}

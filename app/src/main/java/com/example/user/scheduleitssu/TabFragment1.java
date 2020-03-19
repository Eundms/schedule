package com.example.user.scheduleitssu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.utilClass.Today2do;

import java.util.ArrayList;

public class TabFragment1 extends Fragment {
    Context context;
    RecyclerView today2doRecyclerView;
    Today2doAdapter today2doAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View root=inflater.inflate(R.layout.fragment_tab1,container,false);
        this.context=root.getContext();

        today2doRecyclerView=root.findViewById(R.id.today2doRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        today2doRecyclerView.setLayoutManager(layoutManager);
        ArrayList<Today2do>today2doArrayList=new ArrayList<>();
        today2doArrayList.add(new Today2do("1)(default)"));
        today2doArrayList.add(new Today2do("2)(default)"));
        today2doArrayList.add(new Today2do("3)(default)"));
        today2doArrayList.add(new Today2do("4)(default)"));
        today2doArrayList.add(new Today2do("5)(default)"));
        today2doArrayList.add(new Today2do("6)(default)"));
        today2doAdapter=new Today2doAdapter(this.getContext(),today2doArrayList);
        today2doRecyclerView.setAdapter(today2doAdapter);
    return root;
    }
}


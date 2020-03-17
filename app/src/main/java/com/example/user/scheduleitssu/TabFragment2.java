package com.example.user.scheduleitssu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TabFragment2 extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View root=inflater.inflate(R.layout.fragment_tab2,container,false);
       FloatingActionButton addclassbtn=(FloatingActionButton)root.findViewById(R.id.floatingActionButton);
        addclassbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), AddClassNoteActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "floating btn clicked!", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

}

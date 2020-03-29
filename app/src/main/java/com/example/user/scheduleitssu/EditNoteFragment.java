package com.example.user.scheduleitssu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class EditNoteFragment extends Fragment implements View.OnClickListener{
    Context context;
    ImageButton camerabtn;
    ImageButton imageButton;
    Button savebtn;
    public EditNoteFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_edit_note,container,false);
        this.context=root.getContext();

        camerabtn=root.findViewById(R.id.detailnote_camerabtn);
        imageButton=root.findViewById(R.id.editnote_albumbtn);
        savebtn=root.findViewById(R.id.editnote_savebtn);
        camerabtn.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.detailnote_camerabtn:
                Log.d("detailnote","camerbtn");
            case R.id.editnote_albumbtn:
                Log.d("detailnote","albumbtn");
            case R.id.editnote_savebtn:
                Log.d("detailnote","savebtn");
        }
    }
}

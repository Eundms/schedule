package com.example.user.scheduleitssu;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.Editor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.user.scheduleitssu.DataClass.*;

import java.util.HashMap;
import java.util.Map;


public class FirebaseCommunicator {
    private User me;

// 계정의 이름 uid로 이루어진 string
    private FirebaseUser user=null;

    //db root/userPath의 reference
    private String userPath=null;

    private DatabaseReference myRef=null;
    private boolean isEvaluation=false;

    public FirebaseCommunicator(){
        if(user==null)
            user=FirebaseAuth.getInstance().getCurrentUser();
        if(userPath==null)
            userPath=user.getDisplayName()+"_"+user.getUid();
        if(myRef==null)
            myRef = FirebaseDatabase.getInstance().getReference().child("Student").child(userPath);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // me = new User(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public FirebaseCommunicator(final String id, Context context){
        isEvaluation= true;
        userPath=id;

        if(myRef==null){
            myRef=FirebaseDatabase.getInstance().getReference().child("Student").child(userPath);
        }
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   me=new User(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void uploadClass(final Group group){
        myRef.updateChildren((Map<String, Object>) group);
    }
    //수정 필요...
   public void uploadNote(Editor edit){
        String key=myRef.push().getKey();
       /* Map<Integer, String> editmap=new HashMap<>();
        edit.setContentTypeface(editmap);
        HashMap<String,Object> result=new HashMap<>();
        result.put("note",editmap);
        myRef.child("note");*/
       Map<String,Editor> note=new HashMap<>();
       note.put("note1",edit);
       myRef.setValue(note);
   }



}

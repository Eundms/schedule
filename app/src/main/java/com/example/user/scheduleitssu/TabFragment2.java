package com.example.user.scheduleitssu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TabFragment2 extends Fragment implements SubjectAdapter.OnItemClickListener{
    public static final int REQUEST_CODE_ADDCLASS =101;
    public static final int REQUEST_CODE_SUBJECTACTIVITY=102;
    Context context;
    RecyclerView subjectRecyclerView;
    SubjectAdapter subjectAdapter;
    ArrayList<Subject> subjectArrayList;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userid= user.getDisplayName()+"_"+user.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Student").child(userid).child("Subject");


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View root=inflater.inflate(R.layout.fragment_tab2,container,false);
        this.context=root.getContext();

        subjectRecyclerView=root.findViewById(R.id.subjectRecyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this.getContext(),2);
        subjectRecyclerView.setLayoutManager(layoutManager);


        subjectArrayList=new ArrayList<>();
        ArrayList<Note>notelist=new ArrayList<>();
        notelist.add(new Note("{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\" +
                "u003e\\u003cu\\u003eddd\\u003c/u\\u003e\\u003c/p\\u003e\\n\"]," +
                "\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}"));
        notelist.add(new Note("{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\" +
                "u003e\\u003cu\\u003eaaa\\u003c/u\\u003e\\u003c/p\\u003e\\n\"]," +
                "\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}"));


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
             //   Note value = dataSnapshot.getValue(Note.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });




        /*subjectArrayList.add(new Subject(groups,"소프트웨어공학(arraylist)","온라인수업",notelist));//SUBJECTINFO_DEFAULT
        subjectArrayList.add(new Subject("데이터베이스(arraylist)","월,수 1:30~",notelist));//SUBJECTINFO_NOGROUP
        subjectArrayList.add(new Subject(groups,"소프트웨어프로젝트(arraylist)","월,수 2:30~"));//SUBJECTINFO_NONOTE
*/



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

        addListfromfirebase();


        return root;
    }
    public void addListfromfirebase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectArrayList.clear();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) { //하위노드가 없을 떄까지 반복
                    Subject subject = dataSnapshot2.getValue(Subject.class);
                    subjectArrayList.add(subject);
                    //subjectAdapter.notifyItemInserted(subjectArrayList.size()-1);
                }
                subjectAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onItemClick(View v, int pos) {
            Intent intent=new Intent(getContext(),SubjectActivity.class);
            if(subjectArrayList.get(pos).getNotelist()==null){
                intent.putExtra("SubjectInfoType","SUBJECTINFO_NONOTE");
            }else{
                intent.putExtra("SubjectInfoType","SUBJECTINFO_DEFAULT");
                intent.putExtra("DATA",subjectArrayList.get(pos).getNotelist());
            }
            intent.putExtra("subject_info_data",subjectArrayList.get(pos));
            startActivityForResult(intent,REQUEST_CODE_SUBJECTACTIVITY);
    }
}

package com.example.user.scheduleitssu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    DatabaseReference myRef = database.getReference().child("Student").child(userid).child("SubjectList");


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View root=inflater.inflate(R.layout.fragment_tab2,container,false);
        this.context=root.getContext();

        subjectRecyclerView=root.findViewById(R.id.subjectRecyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this.getContext(),2);
        subjectRecyclerView.setLayoutManager(layoutManager);

        subjectArrayList=new ArrayList<>();



        subjectAdapter=new SubjectAdapter(this.getContext(),subjectArrayList);
        subjectAdapter.setOnItemClickListener(this);
        subjectRecyclerView.setAdapter(subjectAdapter);


        FloatingActionButton addclassbtn= root.findViewById(R.id.fb1);
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
        intent.putExtra("EXIST","EXIST");
        intent.putExtra("DATATYPE","SUBJECT");
            if(subjectArrayList.get(pos).getNotelist()==null){
                intent.putExtra("INFOTYPE","SUBJECTINFO_DEFAULT");/*최초 생성되었을 때 그러므로 notelist는 없다*/
            }else {
                intent.putExtra("INFOTYPE", "SUBJECTINFO_NOTE");
            }
        intent.putExtra("DATA",subjectArrayList.get(pos));
        startActivityForResult(intent,REQUEST_CODE_SUBJECTACTIVITY);
    }
}

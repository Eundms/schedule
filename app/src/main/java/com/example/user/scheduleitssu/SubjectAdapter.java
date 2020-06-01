package com.example.user.scheduleitssu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.user.scheduleitssu.DataClass.Subject;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    LayoutInflater inflater;
    private ArrayList<Subject> SubjectArrayList;//여기도 나중에 subject->string으로 바뀌어야함.

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }
    private OnItemClickListener listener=null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public SubjectAdapter(Context context, ArrayList<Subject> SubjectArrayList){
        inflater = LayoutInflater.from(context);
        this.SubjectArrayList = SubjectArrayList;

    }
    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = inflater.inflate(R.layout.subject_item, viewGroup, false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder viewHolder, int position) {
        viewHolder.bindData(SubjectArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return SubjectArrayList.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
            TextView subject_name;
            TextView subject_date;
        public SubjectViewHolder(View itemView) {
            super(itemView);
            subject_name = itemView.findViewById(R.id.subject_item_name);
            subject_date=itemView.findViewById(R.id.subject_item_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(listener!=null){
                            listener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }

        public void bindData(Subject object) {
            if (object == null) return;
            else {
                //itemView각각에 입력하기
               subject_name.setText(object.getClassname());
               subject_date.setText(object.getClassinfo());
            }
        }

    }
}

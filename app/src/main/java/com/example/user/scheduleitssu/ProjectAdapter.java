package com.example.user.scheduleitssu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.DataClass.Group;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    LayoutInflater inflater;
    private ArrayList<Group> ProjectArrayList;//나중에 String으로 바꿔야함.

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }
    private OnItemClickListener listener=null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
    public ProjectAdapter(Context context, ArrayList<Group> ProjectArrayList){
        inflater = LayoutInflater.from(context);
        this.ProjectArrayList = ProjectArrayList;

    }
    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.project_item, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder viewHolder, int position) {
        viewHolder.bindData(ProjectArrayList.get(position));

    }

    @Override
    public int getItemCount() { return ProjectArrayList.size(); }
    public class ProjectViewHolder extends RecyclerView.ViewHolder {
   TextView projectname;
   TextView numofmember;
   TextView recenttime;
        public ProjectViewHolder(View itemView) {
            super(itemView);
            projectname = itemView.findViewById(R.id.project_item_name);
            numofmember =itemView.findViewById(R.id.project_item_numofmember);
            recenttime=itemView.findViewById(R.id.project_item_recenttime);
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

        public void bindData(Group object) {
            if (object == null) return;
            else {
                //itemView각각에 입력하기
                recenttime.setText("원래 시간:이거는 그냥 존재한다~group 에 recent time field를 넣어야하나..");
                projectname.setText(object.getGroupname());
                numofmember.setText(""+object.getNumofmember());

            }
        }

    }

}

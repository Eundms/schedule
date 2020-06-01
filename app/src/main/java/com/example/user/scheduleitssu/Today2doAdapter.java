package com.example.user.scheduleitssu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.user.scheduleitssu.DataClass.What2Do;

import java.util.ArrayList;

public class Today2doAdapter extends RecyclerView.Adapter<Today2doAdapter.Today2doViewHolder> {
    LayoutInflater inflater;
    private ArrayList<What2Do> today2doArrayList;
    private Context context;
    public interface OnTodayItemClickListener{
        void onTodayItemClick(View v, int pos);
    }
    private OnTodayItemClickListener listener=null;
    public void setOnTodayItemClickListener(OnTodayItemClickListener listener){
        this.listener=listener;
    };
    public Today2doAdapter(Context context, ArrayList<What2Do> today2doArrayList) {
        inflater = LayoutInflater.from(context);
        this.today2doArrayList = today2doArrayList;
    }

    @Override
    public Today2doViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = inflater.inflate(R.layout.today2do_item, viewGroup, false);
        return new Today2doViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Today2doViewHolder viewHolder, int position) {
        viewHolder.bindData(today2doArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return today2doArrayList.size();
    }


    public class Today2doViewHolder extends RecyclerView.ViewHolder {
        TextView today2dotext;

        public Today2doViewHolder(View itemView) {
            super(itemView);
            today2dotext = itemView.findViewById(R.id.texttoday2do_item);
        }

        public void bindData(What2Do object) {
            if (object == null) return;
            else {
                today2dotext.setText(object.getWhat2do());
            }
        }

    }

}

package com.example.user.scheduleitssu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.user.scheduleitssu.utilClass.Today2do;

import java.util.ArrayList;

public class Today2doAdapter extends RecyclerView.Adapter<Today2doAdapter.Today2doViewHolder> {
    LayoutInflater inflater;
    private ArrayList<Today2do> today2doArrayList;

    public Today2doAdapter(Context context, ArrayList<Today2do> today2doArrayList) {
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
        CheckBox today2dotext;

        public Today2doViewHolder(View itemView) {
            super(itemView);
            today2dotext = itemView.findViewById(R.id.checktoday2do_item);
        }

        public void bindData(Today2do object) {
            if (object == null) return;
            else {
                today2dotext.setText(object.getWhat2do());
            }
        }

    }

}

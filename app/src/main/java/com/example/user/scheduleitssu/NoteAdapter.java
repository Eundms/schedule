package com.example.user.scheduleitssu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.scheduleitssu.DataClass.Note;
import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorContent;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    private ArrayList<Note> noteArrayList;
    private Context context;
    public interface OnNoteItemClickListener{
        void onNoteItemClick(View v, int pos);
    }
    private OnNoteItemClickListener listener=null;
    public void setOnNoteItemClickListener(OnNoteItemClickListener listener){
        this.listener=listener;
    }

    public NoteAdapter(Context context,ArrayList<Note> notelist){
        this.context=context;
        this.noteArrayList=notelist;
    }


    public ArrayList<Note>getNoteArrayList(){
        return noteArrayList;
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
         Editor shownote;
       TextView notetitle;
        public NoteViewHolder(View itemView){
            super(itemView);
            this.shownote=itemView.findViewById(R.id.shownote);
            this.notetitle=itemView.findViewById(R.id.note_title_subject);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(listener!=null){
                            listener.onNoteItemClick(v,position);
                        }
                    }
                }
            });
        }
    }
    @NonNull
    @Override
   public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.subjectnote_item, parent, false);
        NoteViewHolder viewholder = new NoteViewHolder(view);
        return viewholder;}
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note=noteArrayList.get(position);
        holder.notetitle.setText(note.getTitle());
        String a=note.getContent();
        Log.d("NoteAdapter",a);
        EditorContent Deserialized= holder.shownote.getContentDeserialized(a);
        holder.shownote.render(Deserialized);
    }
    @Override
    public int getItemCount(){
        return this.noteArrayList.size();
    }

}

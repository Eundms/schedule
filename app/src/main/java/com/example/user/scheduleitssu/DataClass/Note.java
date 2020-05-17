package com.example.user.scheduleitssu.DataClass;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    String title;
    String content;

    String whomake_note;
    String whenmake_note;

    String editdate_note;

    public String getEditdate_note() {
        return editdate_note;
    }

    public void setEditdate_note(String editdate_note) {
        this.editdate_note = editdate_note;
    }

    String whoedit_note;
    public String getWhomake_note() {
        return whomake_note;
    }

    public void setWhomake_note(String whomake_note) {
        this.whomake_note = whomake_note;
    }

    public String getWhenmake_note() {
        return whenmake_note;
    }

    public void setWhenmake_note(String whenmake_note) {
        this.whenmake_note = whenmake_note;
    }


    public Note(){
    }
    public Note(String serialize_note){
        this.content=serialize_note;
    }
    public Note( String serialize_note,String title){
        this.content=serialize_note;
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected Note(Parcel in) {
        title=in.readString();
        content =in.readString();
        whomake_note = in.readString();
        whenmake_note = in.readString();
       editdate_note = in.readString();
        whoedit_note = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(whomake_note);
        dest.writeString(whenmake_note);
        dest.writeString(editdate_note);
        dest.writeString(whoedit_note);
    }
}

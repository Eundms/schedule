package com.example.user.scheduleitssu.DataClass;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    String title_note;
    String text_note;
    String img_note;
    String whomake_note;
    String whenmake_note;
    String editdate_note;
    String whoedit_note;

    public Note(String title_note, String text_note, String img_note, String whomake_note, String whenmake_note) {
        this.title_note = title_note;
        this.text_note = text_note;
        this.img_note = img_note;
        this.whomake_note = whomake_note;
        this.whenmake_note = whenmake_note;
    }

    protected Note(Parcel in) {
        title_note = in.readString();
        text_note = in.readString();
        img_note = in.readString();
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

    public String getTitle_note() {
        return title_note;
    }

    public void setTitle_note(String title_note) {
        this.title_note = title_note;
    }

    public String getText_note() {
        return text_note;
    }

    public void setText_note(String text_note) {
        this.text_note = text_note;
    }

    public String getImg_note() {
        return img_note;
    }

    public void setImg_note(String img_note) {
        this.img_note = img_note;
    }

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

    public String getEditdate_note() {
        return editdate_note;
    }

    public void setEditdate_note(String editdate_note) {
        this.editdate_note = editdate_note;
    }

    public String getWhoedit_note() {
        return whoedit_note;
    }

    public void setWhoedit_note(String whoedit_note) {
        this.whoedit_note = whoedit_note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title_note);
        dest.writeString(text_note);
        dest.writeString(img_note);
        dest.writeString(whomake_note);
        dest.writeString(whenmake_note);
        dest.writeString(editdate_note);
        dest.writeString(whoedit_note);
    }
}

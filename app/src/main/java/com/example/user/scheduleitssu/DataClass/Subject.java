package com.example.user.scheduleitssu.DataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Subject implements Parcelable{
    String classname;
    String classinfo;

    //1. Daily notes
    ArrayList<Note> notelist;
    /*2. Group
    ArrayList<String> group;*/
    public Subject(){
    }
    public Subject(String classname){
        this.classname=classname;
    }

    public Subject(String classname,  ArrayList<Note> notelist){
        this(classname);
        this.notelist=notelist;
    }
    public Subject(String classname, String classinfo, ArrayList<Note> notelist){
        this(classname);
        this.classinfo=classinfo;
        this.notelist=notelist;
    }
    protected Subject(Parcel in) {
        classname = in.readString();
        notelist = in.createTypedArrayList(Note.CREATOR);
        classinfo=in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classname);
        dest.writeTypedList(notelist);
        dest.writeString(classinfo);
    }

    /*Getter Setter*/
    public void setNotelist(ArrayList<Note> notelist){
        this.notelist=notelist;
    }
    public ArrayList<Note> getNotelist() {
        return this.notelist;
    }

    public String getClassname() {
        return this.classname;
    }
    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClassinfo() {
        return classinfo;
    }

    public void setClassinfo(String classinfo) {
        this.classinfo = classinfo;
    }

    public static Creator<Subject> getCREATOR() {
        return CREATOR;
    }
}
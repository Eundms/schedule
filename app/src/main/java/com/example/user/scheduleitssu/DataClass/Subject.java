package com.example.user.scheduleitssu.DataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Subject implements Parcelable{
    String classname;
    String classhours;
    //1. Daily notes
    ArrayList<String> notelist;
    //2. Group
    ArrayList<String> group;
    public Subject(String classname){
        this.classname=classname;
        this.classhours="1";
    }
    public Subject(String classname, String classhours){
        this.classname=classname;
        this.classhours=classhours;
    }
    public Subject(String classname, String classhours, ArrayList<String> notelist){
        this(classname,classhours);
        this.notelist=notelist;
    }
    public Subject(ArrayList<String>group, String classname, String classhours){
        this(classname,classhours);
        this.group=group;
    }
    public Subject(ArrayList<String>group, String classname, String classhours, ArrayList<String>notelist){
        this(classname,classhours);
        this.notelist=notelist;
        this.group=group;
    }


    protected Subject(Parcel in) {
        classname = in.readString();
        classhours = in.readString();
        notelist = in.createStringArrayList();
        group = in.createStringArrayList();
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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClasshours() {
        return classhours;
    }

    public void setClasshours(String classhours) {
        this.classhours = classhours;
    }

    public ArrayList<String> getNotelist() {
        return notelist;
    }

    public void setNotelist(ArrayList<String> notelist) {
        this.notelist = notelist;
    }

    public ArrayList<String> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<String> group) {
        this.group = group;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classname);
        dest.writeString(classhours);
        dest.writeStringList(notelist);
        dest.writeStringList(group);
    }
}

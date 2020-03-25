package com.example.user.scheduleitssu.DataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;

public class What2Do implements Parcelable{
    String what2do;
    long date;

    public What2Do(String what2do,long date) {
        this.what2do = what2do;
        this.date=date;
    }
    protected What2Do(Parcel in){
        what2do=in.readString();
        date=in.readLong();
    }

    public static final Creator<What2Do> CREATOR = new Creator<What2Do>() {
        @Override
        public What2Do createFromParcel(Parcel in) {
            return new What2Do(in);
        }

        @Override
        public What2Do[] newArray(int size) {
            return new What2Do[size];
        }
    };

    public String getWhat2do() {
        return what2do;
    }
    public long getDate(){
        return date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(what2do);
        parcel.writeLong(date);
    }
}

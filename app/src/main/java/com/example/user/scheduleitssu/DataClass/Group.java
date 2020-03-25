package com.example.user.scheduleitssu.DataClass;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {// implements Parcelable
    String groupname;
    int numofmember;

    public Group(String groupname, int numofmember) {
        this.groupname = groupname;
        this.numofmember = numofmember;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getNumofmember() {
        return numofmember;
    }

    public void setNumofmember(int numofmember) {
        this.numofmember = numofmember;
    }

    public static Creator<Group> getCREATOR() {
        return CREATOR;
    }

    protected Group(Parcel in) {
        groupname = in.readString();
        numofmember = in.readInt();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupname);
        dest.writeInt(numofmember);
    }
}

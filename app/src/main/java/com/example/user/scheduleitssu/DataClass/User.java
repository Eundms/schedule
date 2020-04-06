package com.example.user.scheduleitssu.DataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class User implements Parcelable {
    //진짜 개인 정보
    String userID;
    String name;
    String email;

    //프로그램 내 정보
    //1. 내가 참여하는 그룹 정보
    ArrayList<String> groupList;
    //2. 내가 만든 노트 리스트
    ArrayList<String>myNoteList;
    //3. 공유 한 노트 리스트
    ArrayList<String>shareNoteList;
    //4. 공유 받은 노트 리스트
    ArrayList<String>sharedNoteList;
    public User(String userID, String name, String email) {
        this.userID = userID;
        this.name = name;
        this.email = email;

    }

    public User(String userID, String name, String email, ArrayList<String> myNoteList) {
        this(userID, name, email);
        this.myNoteList = myNoteList;
    }
    public User(String userID, String name, String email, ArrayList<String> groupList, ArrayList<String> myNoteList, ArrayList<String> shareNoteList, ArrayList<String> sharedNoteList) {
         this(userID, name, email);
        this.groupList = groupList;
        this.myNoteList = myNoteList;
        this.shareNoteList = shareNoteList;
        this.sharedNoteList = sharedNoteList;
    }

    protected User(Parcel in) {
        userID = in.readString();
        name = in.readString();
        email = in.readString();
        groupList = in.createStringArrayList();
        myNoteList = in.createStringArrayList();
        shareNoteList = in.createStringArrayList();
        sharedNoteList = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<String> groupList) {
        this.groupList = groupList;
    }

    public ArrayList<String> getMyNoteList() {
        return myNoteList;
    }

    public void setMyNoteList(ArrayList<String> myNoteList) {
        this.myNoteList = myNoteList;
    }

    public ArrayList<String> getShareNoteList() {
        return shareNoteList;
    }

    public void setShareNoteList(ArrayList<String> shareNoteList) {
        this.shareNoteList = shareNoteList;
    }

    public ArrayList<String> getSharedNoteList() {
        return sharedNoteList;
    }

    public void setSharedNoteList(ArrayList<String> sharedNoteList) {
        this.sharedNoteList = sharedNoteList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeStringList(groupList);
        dest.writeStringList(myNoteList);
        dest.writeStringList(shareNoteList);
        dest.writeStringList(sharedNoteList);
    }
}


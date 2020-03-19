package com.example.user.scheduleitssu.utilClass;

import java.util.ArrayList;

public class User {
    //진짜 개인 정보
    String name;
    String email;
    String major;
    String studentId;
    enum grade {
        FRESHMAN, SOPHOMORE, JUNOR, SENIOR;
    }
    //프로그램 내 정보
    //1. 내가 참여하는 그룹 정보
    //2. 내가 만든 노트 리스트
    //3. 공유 받은 노트 리스트

    public User(String name, String email){
        this.name=name;
        this.email=email;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}


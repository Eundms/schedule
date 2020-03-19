package com.example.user.scheduleitssu.utilClass;

public class Subject {
    //1. Daily notes
    //2. Group
    String name;

    public Subject(String name, String date) {
        this.name = name;
        this.date = date;
    }

    String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

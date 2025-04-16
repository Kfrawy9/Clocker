package com.example.Elkfrawy.model;

public enum Days {


    SAT(1, "Sat"),
    SUN(2, "Sun"), MON(3, "Mon"),
    TUE(4, "Tue"), WED(5, "Wed"),
    THU(6, "Thu"), FRI(7, "Fri");

    int id;
    String name;

    Days(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getValue() { return id; }
    public String getName() { return name; }

}

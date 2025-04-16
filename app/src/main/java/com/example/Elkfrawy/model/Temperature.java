package com.example.Elkfrawy.model;

import com.google.gson.annotations.SerializedName;

public class Temperature {

    @SerializedName("Value")
    int value;

    public int getValue() {
        return value;
    }

}

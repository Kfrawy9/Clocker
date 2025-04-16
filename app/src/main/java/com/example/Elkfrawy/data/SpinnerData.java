package com.example.Elkfrawy.data;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.model.SpinnerItems;

import java.util.ArrayList;
import java.util.List;

public class SpinnerData {

    private static List<SpinnerItems> data = new ArrayList<>();

    public static List<SpinnerItems> loadData(){
        if (data.size() == 0){

            data.add(new SpinnerItems("High", R.color.high));
            data.add(new SpinnerItems("Medium", R.color.medium));
            data.add(new SpinnerItems("Low", R.color.low));

        }

        return data;
    }

}

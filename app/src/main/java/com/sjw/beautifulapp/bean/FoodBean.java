package com.sjw.beautifulapp.bean;

import java.util.List;

/**
 * Created by pc on 2018/9/4.
 */

public class FoodBean {


    private String title;

    private List<FoodInfo> foodInfoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FoodInfo> getFoodInfoList() {
        return foodInfoList;
    }

    public void setFoodInfoList(List<FoodInfo> foodInfoList) {
        this.foodInfoList = foodInfoList;
    }
}

package com.sjw.beautifulapp.bean;

/**
 * Created by pc on 2018/9/4.
 */

public class FoodInfo {

    private String foodImgUrl;
    private String foodName;
    private String foodAuthor;
    private String foodOrigin;
    private String foodDetailUrl;

    public String getFoodDetailUrl() {
        return foodDetailUrl;
    }

    public void setFoodDetailUrl(String foodDetailUrl) {
        this.foodDetailUrl = foodDetailUrl;
    }

    public String getFoodOrigin() {
        return foodOrigin;
    }

    public void setFoodOrigin(String foodOrigin) {
        this.foodOrigin = foodOrigin;
    }


    public String getFoodImgUrl() {
        return foodImgUrl;
    }

    public void setFoodImgUrl(String foodImgUrl) {
        this.foodImgUrl = foodImgUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodAuthor() {
        return foodAuthor;
    }

    public void setFoodAuthor(String foodAuthor) {
        this.foodAuthor = foodAuthor;
    }

}

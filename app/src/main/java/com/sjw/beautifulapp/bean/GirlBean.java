package com.sjw.beautifulapp.bean;

import java.util.ArrayList;

public class GirlBean {


    private String girl_img;
    private String girl_name;
    private String girl_flag;
    private String girl_time;
    private String girl_nomenu;
    private ArrayList<String> detail_imgs;


    public ArrayList<String> getDetail_imgs() {
        return detail_imgs;
    }

    public void setDetail_imgs(ArrayList<String> detail_imgs) {
        this.detail_imgs = detail_imgs;
    }

    public String getGirl_img() {
        return girl_img;
    }

    public void setGirl_img(String girl_img) {
        this.girl_img = girl_img;
    }

    public String getGirl_name() {
        return girl_name;
    }

    public void setGirl_name(String girl_name) {
        this.girl_name = girl_name;
    }

    public String getGirl_flag() {
        return girl_flag;
    }

    public void setGirl_flag(String girl_flag) {
        this.girl_flag = girl_flag;
    }

    public String getGirl_time() {
        return girl_time;
    }

    public void setGirl_time(String girl_time) {
        this.girl_time = girl_time;
    }

    public String getGirl_nomenu() {
        return girl_nomenu;
    }

    public void setGirl_nomenu(String girl_nomenu) {
        this.girl_nomenu = girl_nomenu;
    }
}

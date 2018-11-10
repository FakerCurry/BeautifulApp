package com.sjw.beautifulapp.bean;

import java.io.Serializable;

/**
 * Created by pc on 2018/9/3.
 */

public class EpisodeBean implements Serializable {

    private String title;
    private String imgUrl;
    private String name;
//    private String nameImg;
    private String time;
    private EpisodeDetailBean  detailBean;

    public EpisodeDetailBean getDetailBean() {
        return detailBean;
    }

    public void setDetailBean(EpisodeDetailBean detailBean) {
        this.detailBean = detailBean;
    }

    //    private String timeImg;

//    public String getNameImg() {
//        return nameImg;
//    }
//
//    public void setNameImg(String nameImg) {
//        this.nameImg = nameImg;
//    }
//
//    public String getTimeImg() {
//        return timeImg;
//    }
//
//    public void setTimeImg(String timeImg) {
//        this.timeImg = timeImg;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

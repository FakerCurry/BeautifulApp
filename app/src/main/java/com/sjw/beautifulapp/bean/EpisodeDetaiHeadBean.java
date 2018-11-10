package com.sjw.beautifulapp.bean;

import java.io.Serializable;

public class EpisodeDetaiHeadBean implements Serializable {


     private String headTitle;
     private String headSubname;
     private String headSubtime;

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public String getHeadSubname() {
        return headSubname;
    }

    public void setHeadSubname(String headSubname) {
        this.headSubname = headSubname;
    }

    public String getHeadSubtime() {
        return headSubtime;
    }

    public void setHeadSubtime(String headSubtime) {
        this.headSubtime = headSubtime;
    }
}

package com.sjw.beautifulapp.bean;

import java.io.Serializable;

public class EpisodeDetailText implements Serializable {
    //判断文字是否粗体
    private boolean isBold;
    private String  text;

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

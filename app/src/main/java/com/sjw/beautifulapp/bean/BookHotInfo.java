package com.sjw.beautifulapp.bean;

/**
 * Created by pc on 2018/9/5.
 */

public class BookHotInfo {
    private String hotImg;
    private String hotTitle;
    private String hotAuthor;
    private String detailurl;

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }

    public String getHotImg() {
        return hotImg;
    }

    public void setHotImg(String hotImg) {
        this.hotImg = hotImg;
    }

    public String getHotTitle() {
        return hotTitle;
    }

    public void setHotTitle(String hotTitle) {
        this.hotTitle = hotTitle;
    }

    public String getHotAuthor() {
        return hotAuthor;
    }

    public void setHotAuthor(String hotAuthor) {
        this.hotAuthor = hotAuthor;
    }
}

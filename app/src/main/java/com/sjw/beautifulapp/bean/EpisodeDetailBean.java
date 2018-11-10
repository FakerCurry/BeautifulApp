package com.sjw.beautifulapp.bean;

import java.io.Serializable;
import java.util.List;

public class EpisodeDetailBean implements Serializable {


    //详情页头部的内容
    private EpisodeDetaiHeadBean  headBean;
    //详情页内容的图片
    private List<String> imgUrls;
    //详情页内容的文字
    private List<EpisodeDetailText> textContents;


    public EpisodeDetaiHeadBean getHeadBean() {
        return headBean;
    }

    public void setHeadBean(EpisodeDetaiHeadBean headBean) {
        this.headBean = headBean;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<EpisodeDetailText> getTextContents() {
        return textContents;
    }

    public void setTextContents(List<EpisodeDetailText> textContents) {
        this.textContents = textContents;
    }
}

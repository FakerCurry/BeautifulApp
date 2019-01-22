package com.sjw.beautifulapp.bean;

import java.util.List;

public class Index2HeadBean {
    List<Index2BannerBean>  bannerBeanList;
    List<Index2HeadtabBean>  headtabBeansList;
    String imgUrl;

    public List<Index2BannerBean> getBannerBeanList() {
        return bannerBeanList;
    }

    public void setBannerBeanList(List<Index2BannerBean> bannerBeanList) {
        this.bannerBeanList = bannerBeanList;
    }

    public List<Index2HeadtabBean> getHeadtabBeansList() {
        return headtabBeansList;
    }

    public void setHeadtabBeansList(List<Index2HeadtabBean> headtabBeansList) {
        this.headtabBeansList = headtabBeansList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

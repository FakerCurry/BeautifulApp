package com.sjw.beautifulapp.bean;

import java.util.List;

/**
 * Created by pc on 2018/9/5.
 */

public class IndexBean {

    //轮播图
    List<IndexBannerBean> indexBannerBeans;
    //书籍的分类
    List<BookClassify> bookClassifies;
    //活动快讯
    BookActive bookActive;
    //热门推荐
    BookHot bookHot;
    /**
     * 0.书籍分类 1.快讯 2.热门
     */
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<IndexBannerBean> getIndexBannerBeans() {
        return indexBannerBeans;
    }

    public void setIndexBannerBeans(List<IndexBannerBean> indexBannerBeans) {
        this.indexBannerBeans = indexBannerBeans;
    }

    public List<BookClassify> getBookClassifies() {
        return bookClassifies;
    }

    public void setBookClassifies(List<BookClassify> bookClassifies) {
        this.bookClassifies = bookClassifies;
    }

    public BookActive getBookActive() {
        return bookActive;
    }

    public void setBookActive(BookActive bookActive) {
        this.bookActive = bookActive;
    }

    public BookHot getBookHot() {
        return bookHot;
    }

    public void setBookHot(BookHot bookHot) {
        this.bookHot = bookHot;
    }

}

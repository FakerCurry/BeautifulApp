package com.sjw.beautifulapp.bean;

import java.util.List;

/**
 * Created by pc on 2018/9/5.
 */

public class BookHot {
    private String title;
    private List<BookHotInfo> bookHotInfos;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BookHotInfo> getBookHotInfos() {
        return bookHotInfos;
    }

    public void setBookHotInfos(List<BookHotInfo> bookHotInfos) {
        this.bookHotInfos = bookHotInfos;
    }
}

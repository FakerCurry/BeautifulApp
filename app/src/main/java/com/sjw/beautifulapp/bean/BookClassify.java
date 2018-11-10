package com.sjw.beautifulapp.bean;

import java.util.List;

/**
 * Created by pc on 2018/9/5.
 */

public class BookClassify {
    private String bookInfoType;
    private boolean isSelect;
    private List<BookInfo> bookInfos;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getBookInfoType() {
        return bookInfoType;
    }

    public void setBookInfoType(String bookInfoType) {
        this.bookInfoType = bookInfoType;
    }

    public List<BookInfo> getBookInfos() {
        return bookInfos;
    }

    public void setBookInfos(List<BookInfo> bookInfos) {
        this.bookInfos = bookInfos;
    }
}

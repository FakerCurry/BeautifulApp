package com.sjw.beautifulapp.bean;

import java.util.List;

/**
 * Created by pc on 2018/9/5.
 */

public class BookActive {
    private String title;
    private List<String> content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}

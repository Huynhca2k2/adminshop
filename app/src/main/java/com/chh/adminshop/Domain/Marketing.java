package com.chh.adminshop.Domain;

public class Marketing {
    private String title;
    private String picUrl;
    private String description;

    public Marketing(String title, String picUrl, String description) {
        this.title = title;
        this.picUrl = picUrl;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

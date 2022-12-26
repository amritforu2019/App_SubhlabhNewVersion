package com.vastu.shubhlabhvastu.Model;

public class ModelScreenItem {

    String Title,Description,ScreenImg;

    public ModelScreenItem(String title, String description, String screenImg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setScreenImg(String screenImg) {
        ScreenImg = screenImg;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String  getScreenImg() {
        return ScreenImg;
    }
}

package com.example.magda.theonewsapp;

/**
 * Created by Magda on 27.06.2017.
 */

public class News {
    private String title;
    private String sectionName;

    public News(String title, String sectionName) {
        this.title = title;
        this.sectionName = sectionName;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }
}

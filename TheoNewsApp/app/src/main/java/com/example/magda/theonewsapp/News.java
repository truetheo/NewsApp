package com.example.magda.theonewsapp;

/**
 * Created by Magda on 27.06.2017.
 */

public class News {
    private String title;
    private String sectionName;
    private String webUrl;
    private String webPublicationDate;

    public News(String title, String sectionName, String webUrl, String webPublicationDate) {
        this.title = title;
        this.sectionName = sectionName;
        this.webUrl = webUrl;
        this.webPublicationDate = webPublicationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }
}

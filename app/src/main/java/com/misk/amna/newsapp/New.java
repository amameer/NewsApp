package com.misk.amna.newsapp;

/**
 * Created by Amna on 5/29/2017 AD.
 */

public class New {

    String Title;
    String Url;
    String Section;

    public New(String title, String section, String url) {
        Title = title;
        Url = url;
        Section = section;
    }

    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return Url;
    }

    public String getSection() {
        return Section;
    }

}

package com.misk.amna.newsapp;

/**
 * Created by Amna on 5/29/2017 AD.
 */

public class New {

    String Author;
    String Title;
    String Url;
    String PublishedAt;
    String Description;

    public New(String author, String title, String url, String description) {
        Author = author;
        Title = title;
        Url = url;
       // PublishedAt = publishedAt;
        Description = description;
    }

    public String getAuthor() {
        return Author;
    }

    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return Url;
    }

    public String getPublishedAt() {
        return PublishedAt;
    }

    public String getDescription() {
        return Description;
    }

    @Override
    public String toString() {
        return "New{" +
                "Author='" + Author + '\'' +
                ", Title='" + Title + '\'' +
                ", Url='" + Url + '\'' +
                ", PublishedAt='" + PublishedAt + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }
}

package com.data.cloner.newapp.modelClass;

public class VideoArticle {
    private String title;
    private String link;
    private String thumbnail;

    public VideoArticle(String title, String link, String thumbnail) {
        this.title = title;
        this.link = link;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

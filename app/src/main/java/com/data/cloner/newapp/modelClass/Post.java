package com.data.cloner.newapp.modelClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    public int id;
    public String link;
    public int[] categories;
    public Title title;
    public Content content;

    @SerializedName("_embedded")
    public Embedded embedded;

    public class Title {
        public String rendered;
    }

    public class Content {
        public String rendered;
    }

    public class Embedded {
        @SerializedName("wp:featuredmedia")
        public List<FeaturedMedia> featuredMedia;
    }

    public class FeaturedMedia {
        @SerializedName("source_url")
        public String sourceUrl;
    }


    public String getImageUrl() {
        try {
            return embedded.featuredMedia.get(0).sourceUrl;
        } catch (Exception e) {
            return null;
        }
    }
}
package com.data.cloner.newapp.modelClass;

import com.kwabenaberko.newsapilib.models.Article;

public class CategoryPreview {
    private String category;
    private com.kwabenaberko.newsapilib.models.Article article;


    public CategoryPreview(String category, com.kwabenaberko.newsapilib.models.Article article) {
        this.category = category;
        this.article = article;
    }


    public String getCategory() {
        return category;
    }

    public Article getArticle() {
        return article;
    }
}

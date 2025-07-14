package com.data.cloner.newapp.utils;

import com.data.cloner.newapp.modelClass.Post;

public class CategoryPreviewModel {
    private String category;
    private Post post;

    public CategoryPreviewModel(String category, Post post) {
        this.category = category;
        this.post = post;
    }

    public String getCategory() {
        return category;
    }

    public Post getPost() {
        return post;
    }
}

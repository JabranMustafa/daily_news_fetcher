package com.data.cloner.newapp.modelClass;

public class CategoryPreview  {
    private String category;
    private int categoryId;
    private Post post;

    public CategoryPreview(String category, int categoryId, Post post) {
        this.category = category;
        this.categoryId = categoryId;
        this.post = post;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Post getPost() {
        return post;
    }
}
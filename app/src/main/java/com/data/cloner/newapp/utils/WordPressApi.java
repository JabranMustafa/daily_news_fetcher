package com.data.cloner.newapp.utils;


import com.data.cloner.newapp.modelClass.Category;
import com.data.cloner.newapp.modelClass.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WordPressApi {
    @GET("posts")
    Call<List<Post>> getPosts(@Query("_embed") boolean embed);
    @GET("categories")
    Call<List<Category>> getCategories(@Query("per_page") int perPage);
    @GET("posts")
    Call<List<Post>> getPostsByCategories(
            @Query("_embed") boolean embed,
            @Query("categories") String categoryIdsCsv
    );

}
package com.data.cloner.newapp.utils;

import android.widget.TextView;

import com.data.cloner.newapp.modelClass.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsTickerManager {

    public static void fetchNewsAndSetTicker(TextView tickerView) {
        WordPressApi api = ApiClient.getClient().create(WordPressApi.class);
        Call<List<Post>> call = api.getPosts(true);  // include _embed if needed

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder tickerText = new StringBuilder("  Breaking: ");
                    for (int i = 0; i < Math.min(10, response.body().size()); i++) {
                        tickerText.append(response.body().get(i).title.rendered).append("  ");
                    }
                    tickerView.setText(tickerText.toString());

                    tickerView.setSelected(false);
                    tickerView.setSelected(true);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tickerView.setText("Failed to load news.");
            }
        });
    }
}
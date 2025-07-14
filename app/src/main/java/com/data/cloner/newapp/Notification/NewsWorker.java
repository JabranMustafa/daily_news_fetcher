package com.data.cloner.newapp.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.data.cloner.newapp.Activities.NewsActivity;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.utils.ApiClient;
import com.data.cloner.newapp.modelClass.Post;
import com.data.cloner.newapp.utils.WordPressApi;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsWorker extends Worker {
    public NewsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        CountDownLatch latch = new CountDownLatch(1);

        WordPressApi api = ApiClient.getClient().create(WordPressApi.class);
        Call<List<Post>> call = api.getPosts(true); // using _embed=true

        SharedPreferences prefs = context.getSharedPreferences("news_prefs", Context.MODE_PRIVATE);
        String lastTitle = prefs.getString("last_title", "");

        final Result[] result = {Result.success()};

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Post post : response.body()) {
                        String title = post.title.rendered;
                        String content = post.content.rendered;
                        String link = post.link;

                        if (!title.equals(lastTitle)) {
                            sendNotification(context, title, stripHtml(content), link);
                            prefs.edit().putString("last_title", title).apply();
                            break;
                        }
                    }
                } else {
                    result[0] = Result.failure();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                result[0] = Result.failure();
                latch.countDown();
            }
        });

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return Result.failure();
        }

        return result[0];
    }

    private void sendNotification(Context context, String title, String message, String url) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "news_channel")
                .setSmallIcon(R.drawable.newlogo)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private String stripHtml(String html) {
        return html.replaceAll("<[^>]*>", "").trim();
    }
}
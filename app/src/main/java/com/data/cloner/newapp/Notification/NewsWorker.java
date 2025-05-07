package com.data.cloner.newapp.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.data.cloner.newapp.Activities.WebView_Notification;
import com.data.cloner.newapp.Fragments.WebViewFragment;
import com.data.cloner.newapp.R;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NewsWorker extends Worker {
    public NewsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        CountDownLatch latch = new CountDownLatch(1);
        NewsApiClient client = new NewsApiClient("614c81fb00e0498e8f1ab46c0f5fae87");

        TopHeadlinesRequest request = new TopHeadlinesRequest.Builder()
                .language("en")
                .pageSize(10)
                .build();

        client.getTopHeadlines(request, new NewsApiClient.ArticlesResponseCallback() {
            @Override
            public void onSuccess(ArticleResponse response) {
                List<Article> articles = response.getArticles();
                Context context = getApplicationContext();
                SharedPreferences prefs = context.getSharedPreferences("news_prefs", Context.MODE_PRIVATE);
                String lastTitle = prefs.getString("last_title", "");

                for (Article article : articles) {
                    if (article.getTitle() != null && article.getUrl() != null && !article.getTitle().equals(lastTitle)) {
                        sendNotification(article.getTitle(), article.getDescription(), article.getUrl());

                        // Save latest as last notified
                        prefs.edit().putString("last_title", article.getTitle()).apply();
                        break;
                    }
                }

                latch.countDown();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("NewsWorker", "Error: " + throwable.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return Result.failure();
        }

        return Result.success();
    }

    private void sendNotification(String title, String message, String url) {
        Context context = getApplicationContext();

        Intent intent = new Intent(context, WebView_Notification.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d("NewsWorker", "Sending notification for URL: " + url);
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
}

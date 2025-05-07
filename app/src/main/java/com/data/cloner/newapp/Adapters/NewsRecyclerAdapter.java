package com.data.cloner.newapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.data.cloner.newapp.Fragments.WebViewFragment;
import com.data.cloner.newapp.R;
import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter  extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private FragmentManager fragmentManager;
    List<Article> articleList;

    public NewsRecyclerAdapter(List<Article> articleList, FragmentManager fm) {

        this.articleList = articleList;
        this.fragmentManager = fm;
    }



@NonNull
@Override
public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row,parent,false);
    return new NewsViewHolder(view);
}

@Override
public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
    com.kwabenaberko.newsapilib.models.Article article = articleList.get(position);
    if (article != null) {
    holder.titleTextView.setText(article.getTitle());
    holder.sourceTextView.setText(article.getSource().getName());
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Picasso.get().load(article.getUrlToImage())
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);}
    }

    holder.itemView.setOnClickListener((v -> {

        Fragment fragment = WebViewFragment.newInstance(article.getUrl());
        ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();


    }));

 /*       Fragment fragment = WebViewFragment.newInstance(article.getUrl());

        ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

    }));*/

}

public void updateData(List<Article> data){
    articleList.clear();
    articleList.addAll(data);
    notifyDataSetChanged();

}

@Override
public int getItemCount() {
    return articleList.size();
}

class NewsViewHolder extends RecyclerView.ViewHolder{

    TextView titleTextView,sourceTextView;
    ImageView imageView;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.article_title);
        sourceTextView = itemView.findViewById(R.id.article_source);
        imageView = itemView.findViewById(R.id.article_image_view);
    }
}
}

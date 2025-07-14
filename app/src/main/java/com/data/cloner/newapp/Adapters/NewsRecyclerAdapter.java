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
import com.data.cloner.newapp.modelClass.Post;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private FragmentManager fragmentManager;
    List<Post> postList;

    public NewsRecyclerAdapter(List<Post> postList, FragmentManager fm) {

        this.postList = postList;
        this.fragmentManager = fm;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Post post = postList.get(position);
        if (post != null && holder.titleTextView != null) {
            holder.titleTextView.setText(post.title.rendered);

            String imageUrl = post.getImageUrl();

            if (holder.imageView != null && imageUrl != null) {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.imageView);
            }

            if (holder.sourceTextView != null) {
                holder.sourceTextView.setText("المصدر");  // Optional: Replace with category/source
            }

            holder.itemView.setOnClickListener((v -> {
                Fragment fragment = WebViewFragment.newInstance(post.link);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }));
        }


    }

    public void updateData(List<Post> data) {
        postList.clear();
        postList.addAll(data);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, sourceTextView;
        ShapeableImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);
        }
    }
}

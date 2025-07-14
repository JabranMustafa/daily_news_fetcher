package com.data.cloner.newapp.Adapters;

import android.graphics.drawable.shapes.Shape;
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

public class FeaturedNewsAdapter extends RecyclerView.Adapter<FeaturedNewsAdapter.FeaturedViewHolder> {

    private List<Post> featuredPosts;
    private FragmentManager fragmentManager;

    public FeaturedNewsAdapter(List<Post> posts, FragmentManager fm) {
        this.featuredPosts = posts;
        this.fragmentManager = fm;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured_news, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        Post post = featuredPosts.get(position);
        holder.title.setText(post.title.rendered);
        Picasso.get().load(post.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Fragment fragment = WebViewFragment.newInstance(post.link);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return featuredPosts.size();
    }

    class FeaturedViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView image;
        TextView title;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
        }
    }
}
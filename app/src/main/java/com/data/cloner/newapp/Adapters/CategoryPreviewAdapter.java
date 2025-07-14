package com.data.cloner.newapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.data.cloner.newapp.Fragments.CategoryListFragment;
import com.data.cloner.newapp.modelClass.CategoryPreview;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.modelClass.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryPreviewAdapter extends  RecyclerView.Adapter<CategoryPreviewAdapter.ViewHolder> {

    private Context context;
    private List<CategoryPreview> items;

    public CategoryPreviewAdapter(Context context, List<CategoryPreview> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryPreview preview = items.get(position);
//        Article article = preview.getArticle();
        Post post = preview.getPost();
        String imageUrl = post.getImageUrl();
        if (post != null) {
            holder.title.setText(post.title.rendered);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)  // Optional: fallback while loading
                    .error(R.drawable.ic_launcher_background)       // Optional: if load fails
                    .into(holder.imageView);
            holder.itemView.setOnClickListener(v -> {
                CategoryListFragment fragment = CategoryListFragment.newInstance(
                        preview.getCategory(),  // slug or label
                        preview.getCategoryId() // real numeric category ID from WordPress
                );
                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        } else {
            holder.title.setText("No post available");
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
            holder.itemView.setOnClickListener(null);
        }
    }




    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            imageView = itemView.findViewById(R.id.ivImage);
        }
    }
}

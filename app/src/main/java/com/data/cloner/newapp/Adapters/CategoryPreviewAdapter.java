package com.data.cloner.newapp.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.kwabenaberko.newsapilib.models.Article;
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
        Article article = preview.getArticle();
        if (article != null) {
            holder.title.setText(article.getTitle());

            if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
                Picasso.get().load(article.getUrlToImage()).into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_launcher_background); // fallback image
            }

            holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(context, CategoryListFragment.class);
//                intent.putExtra("category",preview.getCategory());
//
//                context.startActivity(intent);
                CategoryListFragment fragment = CategoryListFragment.newInstance(preview.getCategory());

                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
        else {
            // Optional: hide this item or show a placeholder
            holder.title.setText("No article available");
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
            holder.itemView.setOnClickListener(null); // disable click
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

package com.data.cloner.newapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.data.cloner.newapp.Fragments.WebViewFragment;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.modelClass.VideoArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoNewsAdapter extends RecyclerView.Adapter<VideoNewsAdapter.VideoViewHolder> {

    private Context context;
    private List<VideoArticle> videoList;

    public VideoNewsAdapter(Context context, List<VideoArticle> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_news, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoArticle video = videoList.get(position);
        holder.title.setText(video.getTitle());
        Picasso.get().load(video.getThumbnail()).into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {

            Fragment fragment = WebViewFragment.newInstance(video.getLink());
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumbnail;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            thumbnail = itemView.findViewById(R.id.thumbnailImage);
        }
    }
}

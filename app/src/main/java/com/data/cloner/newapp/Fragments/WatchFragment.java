package com.data.cloner.newapp.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.modelClass.VideoArticle;
import com.data.cloner.newapp.Adapters.VideoNewsAdapter;
import com.data.cloner.newapp.utils.NewsTickerManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchFragment extends Fragment {
    RecyclerView recyclerView;
    List<VideoArticle> videoList;
    TextView tickerView;
    VideoNewsAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchFragment newInstance(String param1, String param2) {
        WatchFragment fragment = new WatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        tickerView = view.findViewById(R.id.breakingNewsTicker);
        NewsTickerManager.fetchNewsAndSetTicker(tickerView);
        videoList = new ArrayList<>();
        adapter = new VideoNewsAdapter(getContext(), videoList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchVideoFeed();
        // Inflate the layout for this fragment
        return view;
    }

    private void fetchVideoFeed() {
        String url = "";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.has("youtube_live_url")) {
                            String liveUrl = response.getString("youtube_live_url");

                            // You can extract video ID or title if needed
                            String title = "YouTube Live Stream";
                            String link = liveUrl;
                            String thumbnail = "" +
                                    Uri.parse(liveUrl).getQueryParameter("v") + "/hqdefault.jpg";

                            videoList.clear(); // optional: clear old videos
                            videoList.add(new VideoArticle(title, link, thumbnail));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.e("JSONError", e.getMessage());
                    }
                },
                error -> Log.e("FeedError", error.toString())
        );

        queue.add(request);
    }



}
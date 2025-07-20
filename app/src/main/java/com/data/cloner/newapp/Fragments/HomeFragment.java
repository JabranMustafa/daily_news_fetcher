package com.data.cloner.newapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.data.cloner.newapp.Activities.NewsActivity;
import com.data.cloner.newapp.Adapters.FeaturedNewsAdapter;
import com.data.cloner.newapp.Adapters.NewsRecyclerAdapter;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.utils.ApiClient;
import com.data.cloner.newapp.utils.NewsTickerManager;
import com.data.cloner.newapp.modelClass.Post;
import com.data.cloner.newapp.utils.WordPressApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsRecyclerAdapter adapter;
    private ViewPager2 featuredViewPager;
    private FeaturedNewsAdapter featuredAdapter;
    private TextView tickerView;

    private final List<Post> postList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        tickerView = view.findViewById(R.id.breakingNewsTicker);
        recyclerView = view.findViewById(R.id.news_recycler_view);
        featuredViewPager = view.findViewById(R.id.featured_viewpager);

        featuredViewPager.setClipToPadding(false);
        featuredViewPager.setClipChildren(false);
        featuredViewPager.setOffscreenPageLimit(3);
        featuredViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(24)); // spacing between items
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.95f + r * 0.05f); // optional zoom effect
        });
        featuredViewPager.setPageTransformer(transformer);
        // Set up ticker
        if (tickerView != null) {
            NewsTickerManager.fetchNewsAndSetTicker(tickerView);
        }

        // Layout for news list
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Start fetching content
       fetchPosts();

        return view;


    }


private void fetchPosts() {
    if (!isAdded()) return;

    SharedPreferences prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
    String categoryIdsCsv = prefs.getString("last_toggled_categories", null);

    WordPressApi api = ApiClient.getClient().create(WordPressApi.class);
    Call<List<Post>> call = (categoryIdsCsv != null && categoryIdsCsv.matches("\\d+(,\\d+)*"))
            ? api.getPostsByCategories(true, categoryIdsCsv)
            : api.getPosts(true);

    call.enqueue(new Callback<List<Post>>() {
        @Override
        public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
            if (!isAdded() || getContext() == null || response.body() == null) return;

            List<Post> data = response.body();

            if (data.isEmpty()) {
                Log.e("HomeFragment", "No posts returned from API");
                return;
            }

            postList.clear();
            postList.addAll(data);

            // Defensive safety: check view references are valid
            if (featuredViewPager == null || recyclerView == null) {
                Log.e("HomeFragment", "ViewPager or RecyclerView not initialized");
                return;
            }

            List<Post> featuredList = data.size() >= 5
                    ? data.subList(0, 5)
                    : new ArrayList<>(data);

            // Use try-catch to trap UI crashes
            try {
                featuredAdapter = new FeaturedNewsAdapter(featuredList, getParentFragmentManager());
                featuredViewPager.setAdapter(featuredAdapter);
                featuredViewPager.post(() -> {
                    featuredViewPager.setCurrentItem(1, false);
                    featuredViewPager.setCurrentItem(0, false);
                });

                adapter = new NewsRecyclerAdapter(data, getParentFragmentManager());
                recyclerView.setAdapter(adapter);

                Log.d("DEBUG", "Featured: " + featuredList.size() + " | AllPosts: " + postList.size());
            } catch (Exception e) {
                Log.e("HomeFragment", "Adapter or view setup crash: ", e);
            }
        }

        @Override
        public void onFailure(Call<List<Post>> call, Throwable t) {
            Log.e("HomeFragment", "Network/API error: ", t);
        }
    });
}
@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NewsActivity) {
            ((NewsActivity) context).setChannelChangeListener(() -> fetchPosts());
        }
    }

    @Override
    public void onDetach() {
        if (getActivity() instanceof NewsActivity) {
            ((NewsActivity) getActivity()).setChannelChangeListener(null);
        }
        super.onDetach();
    }

}
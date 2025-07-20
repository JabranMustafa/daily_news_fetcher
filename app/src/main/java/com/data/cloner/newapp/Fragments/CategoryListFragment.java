package com.data.cloner.newapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.data.cloner.newapp.Adapters.NewsRecyclerAdapter;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.utils.ApiClient;
import com.data.cloner.newapp.utils.NewsTickerManager;
import com.data.cloner.newapp.modelClass.Post;
import com.data.cloner.newapp.utils.WordPressApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsRecyclerAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private String categorySlug;
    TextView tickerView;
    private int categoryId = 0;
    //    private List<Article> articleList = new ArrayList<>();
//    private String category;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CategoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryListFragment newInstance(String categorySlug, int categoryId) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString("category", categorySlug);
        args.putInt("category_id", categoryId);
        fragment.setArguments(args);
        return fragment;
    }

//    public static CategoryListFragment newInstance(String param1, String param2) {
//        CategoryListFragment fragment = new CategoryListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categorySlug = getArguments().getString("category");
            categoryId = getArguments().getInt("category_id", 0);
        }
//        if (getArguments() != null) {
////            categorySlug = getArguments().getString("category");
//
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        recyclerView = view.findViewById(R.id.news_recycler_view);
        tickerView = view.findViewById(R.id.breakingNewsTicker);
        NewsTickerManager.fetchNewsAndSetTicker(tickerView);
//        progressIndicator = view.findViewById(R.id.progress_bar);

        if (getArguments() != null) {
            categorySlug = getArguments().getString("category");
            categoryId = getArguments().getInt("category_id", 0);
        }

        setupRecyclerView();
        fetchCategoryPosts();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        adapter = new NewsRecyclerAdapter(articleList, getParentFragmentManager());
        adapter = new NewsRecyclerAdapter(postList, getParentFragmentManager());
        recyclerView.setAdapter(adapter);
    }

    private void fetchCategoryPosts() {

        WordPressApi api = ApiClient.getClient().create(WordPressApi.class);
        Call<List<Post>> call = api.getPosts(true);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("CAT_LIST", "Filtering by ID: " + categoryId);
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    for (Post post : response.body()) {
                        Log.d("CAT_LIST", "Post: " + post.id + " Categories: " + Arrays.toString(post.categories));

                        if (post.categories != null) {
                            for (int id : post.categories) {
                                if (id == categoryId) {
                                    postList.add(post);
                                    break;
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}




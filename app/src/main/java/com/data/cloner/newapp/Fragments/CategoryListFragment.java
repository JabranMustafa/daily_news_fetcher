package com.data.cloner.newapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.data.cloner.newapp.Adapters.NewsRecyclerAdapter;
import com.data.cloner.newapp.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsRecyclerAdapter adapter;
//    private LinearProgressIndicator progressIndicator;
    private List<Article> articleList = new ArrayList<>();
    private String category;


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
    public static CategoryListFragment newInstance(String category) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }
    public static CategoryListFragment newInstance(String param1, String param2) {
        CategoryListFragment fragment = new CategoryListFragment();
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
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_category_list, container, false);
        recyclerView = view.findViewById(R.id.news_recycler_view);
//        progressIndicator = view.findViewById(R.id.progress_bar);

        if (getArguments() != null) {
            category = getArguments().getString("category");
        }

        setupRecyclerView();
        getNews(category, null);

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        adapter = new NewsRecyclerAdapter(articleList, getParentFragmentManager());
        adapter = new NewsRecyclerAdapter( articleList, getParentFragmentManager());
        recyclerView.setAdapter(adapter);
    }

//    private void changeInProgress(boolean show) {
//        if (show)
//            progressIndicator.setVisibility(View.VISIBLE);
//        else
//            progressIndicator.setVisibility(View.INVISIBLE);
//    }

    private void getNews(String category, String query) {
//        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("614c81fb00e0498e8f1ab46c0f5fae87");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category.toLowerCase())
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (response.getArticles() == null || response.getArticles().isEmpty()) {
//                            changeInProgress(false);
                            if (isAdded()) {
                                Toast.makeText(requireContext(), "API limit reached or no news available.", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        if (!isAdded() || getActivity() == null || response == null || response.getArticles() == null) return;
                        requireActivity().runOnUiThread(() -> {
                            if (!isAdded() || getActivity() == null || response == null || response.getArticles() == null) return;
                            if (response == null || response.getArticles() == null || response.getArticles().isEmpty()) {
//                                changeInProgress(false);
                                Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            articleList.clear();
                            articleList.addAll(response.getArticles());
//                            articleList = response.getArticles();
//                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();
//                            changeInProgress(false);
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() -> {
//                            changeInProgress(false);
                            Log.i("GOT Failure", throwable.getMessage());
                        });
                    }
                }
        );
    }
}


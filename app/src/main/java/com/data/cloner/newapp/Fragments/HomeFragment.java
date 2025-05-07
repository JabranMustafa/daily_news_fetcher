package com.data.cloner.newapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.data.cloner.newapp.Adapters.NewsRecyclerAdapter;
import com.data.cloner.newapp.R;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

//    eb168fa54f674a859df5a71478fb2208
    private RecyclerView recyclerView;
    private NewsRecyclerAdapter adapter;
    private List<Article> articleList;
//    private ProgressBar loading;

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

            recyclerView = view.findViewById(R.id.news_recycler_view);
//        loading = view.findViewById(R.id.progress_bar);
            articleList = new ArrayList<>();
            adapter = new NewsRecyclerAdapter(articleList,getChildFragmentManager());
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(adapter);

            getNews("general", null);

            return view;



        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }



void getNews(String category,String query){
//        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("614c81fb00e0498e8f1ab46c0f5fae87");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en").category(category)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

//                        requireActivity().runOnUiThread(()->{
//                            Log.i("GOT Success",response.toString());
//                            changeInProgress(false);
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();
//                        });

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        try {

                        } catch (Exception e) {
//                            throw new RuntimeException(e);
                        }
//                        Log.i("GOT Failure",throwable.getMessage());
                    }
                }
        );
    }
}
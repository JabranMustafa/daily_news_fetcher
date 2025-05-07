package com.data.cloner.newapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.data.cloner.newapp.modelClass.CategoryPreview;
import com.data.cloner.newapp.Adapters.CategoryPreviewAdapter;
import com.data.cloner.newapp.R;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicsFragment extends Fragment {
       RecyclerView recyclerView;
    private final String[] categories = {
            "general", "sports", "business", "technology", "entertainment", "health", "science"
    };
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TopicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopicsFragment newInstance(String param1, String param2) {
        TopicsFragment fragment = new TopicsFragment();
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
        View view =inflater.inflate(R.layout.fragment_topics, container, false);
      recyclerView =view.findViewById(R.id.category_main_recycler_view);
        loadCategoryPreviews();
        return view;
    }
    private void loadCategoryPreviews() {
        List<CategoryPreview> previews = new ArrayList<>();
        CategoryPreviewAdapter adapter = new CategoryPreviewAdapter(getContext(), previews);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        for (String category : categories) {
            NewsApiClient client = new NewsApiClient("614c81fb00e0498e8f1ab46c0f5fae87");

            TopHeadlinesRequest request = new TopHeadlinesRequest.Builder()
                    .language("en")
                    .category(category)
                    .pageSize(1) // Only get one article
                    .build();

//            client.getTopHeadlines(request, new NewsApiClient.ArticlesResponseCallback() {
//                @Override
//                public void onSuccess(ArticleResponse response) {
//                    runOnUiThread(() -> {
//                        if (response.getArticles() != null && !response.getArticles().isEmpty()) {
//                            Article first = response.getArticles().get(0);
//                            previews.add(new CategoryPreview(category, first));
//                            adapter.notifyDataSetChanged();
//                        }
//                    });
//                }
            client.getTopHeadlines(request, new NewsApiClient.ArticlesResponseCallback() {
                @Override
                public void onSuccess(ArticleResponse response) {
                    if (response.getArticles() != null && !response.getArticles().isEmpty()) {
                            Article first = response.getArticles().get(0);
                            previews.add(new CategoryPreview(category, first));
                            adapter.notifyDataSetChanged();
                        }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    try {

//                            Toast.makeText(getContext(), "error loading", Toast.LENGTH_SHORT).show();
                        } catch (RuntimeException e) {
//                        throw new RuntimeException(e);
                    }
                    }


            });

//                @Override
//                public void onFailure(Throwable throwable) {
//                    runOnUiThread(() ->
//                            Toast.makeText(getContext(), "Error loading " + category, Toast.LENGTH_SHORT).show()
//                    );
//                }
//            });
        }
    }
}
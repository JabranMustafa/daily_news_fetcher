package com.data.cloner.newapp.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.data.cloner.newapp.Adapters.NewsRecyclerAdapter;
import com.data.cloner.newapp.modelClass.CategoryPreview;
import com.data.cloner.newapp.Adapters.CategoryPreviewAdapter;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.utils.ApiClient;
import com.data.cloner.newapp.modelClass.Category;
import com.data.cloner.newapp.utils.NewsTickerManager;
import com.data.cloner.newapp.modelClass.Post;
import com.data.cloner.newapp.utils.WordPressApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicsFragment extends Fragment {
    RecyclerView recyclerView;
    SearchView searchView;
    TextView tickerView;
    private List<CategoryPreview> previews = new ArrayList<>();
    private Map<String, Integer> categoryMap = new HashMap<>();

    // Map of category names to WordPress category IDs (you must replace these with real values from your site)

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
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        recyclerView = view.findViewById(R.id.category_main_recycler_view);
        tickerView = view.findViewById(R.id.breakingNewsTicker);
        NewsTickerManager.fetchNewsAndSetTicker(tickerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchView = view.findViewById(R.id.search_view);
      fetchCategoriesAndThenPosts();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchSearchedPosts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void fetchCategoriesAndThenPosts() {
        WordPressApi api = ApiClient.getClient().create(WordPressApi.class);

        api.getCategories(100).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Category cat : response.body()) {
                        categoryMap.put(cat.slug, cat.id);
                    }
                    fetchCategoryPreviews();
                } else {
//                    Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCategoryPreviews() {
        WordPressApi api = ApiClient.getClient().create(WordPressApi.class);

        api.getPosts(true).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!isAdded() || getContext() == null) return;
                if (response.isSuccessful() && response.body() != null) {
                    previews.clear();
                    Set<Integer> addedCategoryIds = new HashSet<>();

                    for (Post post : response.body()) {
                        if (post.categories != null) {
                            for (int catId : post.categories) {
                                if (!addedCategoryIds.contains(catId)) {
                                    // get slug by ID
                                    String categorySlug = getCategorySlugById(catId);
                                    if (categorySlug != null) {
                                        previews.add(new CategoryPreview(categorySlug, catId, post));
                                        addedCategoryIds.add(catId);
                                    }
                                }
                            }
                        }
                    }

                    if (getContext() != null) {
                        CategoryPreviewAdapter adapter = new CategoryPreviewAdapter(getContext(), previews);
                        recyclerView.setAdapter(adapter);
                    }
                    if (previews.isEmpty()) {
//                        Toast.makeText(getContext(), "No posts found per category", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                if (getContext() != null){
//                    Toast.makeText(getContext(), "Failed to fetch posts", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void fetchSearchedPosts(String query) {
        WordPressApi api = ApiClient.getClient().create(WordPressApi.class);
        api.getPosts(true).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> results = new ArrayList<>();
                    for (Post post : response.body()) {
                        if (post.title != null && post.title.rendered.toLowerCase().contains(query.toLowerCase())) {
                            results.add(post);
                        }
                    }

                    NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(results, getChildFragmentManager());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
//                Toast.makeText(getContext(), "Search failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCategorySlugById(int id) {
        for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
            if (entry.getValue() == id) return entry.getKey();
        }
        return null;
    }


}


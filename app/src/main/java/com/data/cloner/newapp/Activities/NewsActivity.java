package com.data.cloner.newapp.Activities;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.data.cloner.newapp.Fragments.HomeFragment;
import com.data.cloner.newapp.Fragments.ProfileFragment;
import com.data.cloner.newapp.Fragments.TopicsFragment;
import com.data.cloner.newapp.Fragments.WatchFragment;
import com.data.cloner.newapp.Notification.NewsWorker;
import com.data.cloner.newapp.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;


public class NewsActivity extends AppCompatActivity {

//    RecyclerView recyclerView;
//    List<Article> articleList = new ArrayList<>();
//    NewsRecyclerAdapter adapter;
//    LinearProgressIndicator progressIndicator;
//    GoogleSignInOptions gso;
//    GoogleSignInClient gsc;
//
//    AppCompatButton signOutBtn;

   String API_KEY = "eb168fa54f674a859df5a71478fb2208";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        scheduleNewsWorker();
//        recyclerView = findViewById(R.id.news_recycler_view);
//        progressIndicator = findViewById(R.id.progress_bar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Set up listener for tab switching
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_video:
                    selectedFragment = new WatchFragment();
                    break;
                case R.id.nav_Category:
                    selectedFragment = new TopicsFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.nav_home:
                default:
                    selectedFragment = new HomeFragment();
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });


        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    private void scheduleNewsWorker() {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                NewsWorker.class, 15, TimeUnit.MINUTES
        ).build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                "news_check", ExistingPeriodicWorkPolicy.KEEP, request
        );
    }

    // Set default tab from intent
       /* String tab = getIntent().getStringExtra("tab");
        if (tab == null) tab = "home";

        switch (tab) {
            case "watch":
                bottomNav.setSelectedItemId(R.id.nav_video);
                break;
            case "topics":
                bottomNav.setSelectedItemId(R.id.nav_Category);
                break;
            case "profile":
                bottomNav.setSelectedItemId(R.id.nav_profile);
                break;
            default:
                bottomNav.setSelectedItemId(R.id.nav_home);
                break;
        }*/
        // Load default fragment
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//        setupRecyclerView();
//        getNews("GENERAL",null);

/*        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/




//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc = GoogleSignIn.getClient(this,gso);

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
     /*   if(acct!=null){

        }
*/
     /*   signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/



    }

/*    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(NewsActivity.this,Login.class));
            }
        });
    }*/
//    void setupRecyclerView(){
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new NewsRecyclerAdapter(articleList);
//        recyclerView.setAdapter(adapter);
//    }
//
//    void changeInProgress(boolean show){
//        if(show)
//            progressIndicator.setVisibility(View.VISIBLE);
//        else
//            progressIndicator.setVisibility(View.INVISIBLE);
//    }


//    void getNews(String category,String query){
//        changeInProgress(true);
//        NewsApiClient newsApiClient = new NewsApiClient("eb168fa54f674a859df5a71478fb2208");
//        newsApiClient.getTopHeadlines(
//                new TopHeadlinesRequest.Builder()
//                        .language("en").category(category)
//                        .q(query)
//                        .build(),
//                new NewsApiClient.ArticlesResponseCallback() {
//                    @Override
//                    public void onSuccess(ArticleResponse response) {
//
//                        runOnUiThread(()->{
//                            Log.i("GOT Success",response.toString());
//                            changeInProgress(false);
//                            articleList = response.getArticles();
//                            adapter.updateData(articleList);
//                            adapter.notifyDataSetChanged();
//                        });
//
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        Log.i("GOT Failure",throwable.getMessage());
//                    }
//                }
//        );
//    }
//    public void newsClick(View view) {
//        Toast.makeText(this, "I am working", Toast.LENGTH_SHORT).show();
//        // handle home click
//    }
//
//    public void watchClick(View view) {
//        Intent intent = new Intent(NewsActivity.this, WatchVideoActivity.class);
//        startActivity(intent);
//        // handle search click
//    }
//
//    public void topicClick(View view) {
//        Intent intent = new Intent(NewsActivity.this, CategoryMain.class);
//        startActivity(intent);
//
//        // handle bookmark click
//    }
//
//    public void onProfileClick(View view) {
//        Toast.makeText(this, "I am working", Toast.LENGTH_SHORT).show();
//
//        // handle profile click
//    }

//    @Override
//    public void onClick(View v) {
//        Button btn = (Button) v;
//        String category = btn.getText().toString();
//        getNews(category,null);
//    }



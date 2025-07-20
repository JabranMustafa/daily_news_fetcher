package com.data.cloner.newapp.Activities;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.data.cloner.newapp.Fragments.HomeFragment;
import com.data.cloner.newapp.Fragments.ProfileFragment;
import com.data.cloner.newapp.Fragments.TopicsFragment;
import com.data.cloner.newapp.Fragments.WatchFragment;
import com.data.cloner.newapp.Fragments.WebViewFragment;
import com.data.cloner.newapp.Notification.NewsWorker;
import com.data.cloner.newapp.R;

import com.data.cloner.newapp.utils.Channel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class NewsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private DrawerLayout drawerLayout;
    boolean firstLaunch = true;
    SharedPreferences prefs;
    SwitchCompat switchDarkMode;
    NavigationView navView;

    String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);

        createNotificationChannel();
        scheduleNewsWorker();

        bottomNav = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        MenuItem darkModeItem = navView.getMenu().findItem(R.id.dark_mode);
        View actionView = darkModeItem.getActionView();
        switchDarkMode = actionView.findViewById(R.id.switch_dark_mode);
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        darkModeClick();
       channelMapping();


        // Handle Bottom Nav Tabs
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
                    selectedFragment = new HomeFragment();

                default:
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

        // Handle Navigation Drawer
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_english:
                    setLocale("en");
                    break;
                case R.id.nav_arabic:
                    setLocale("ar");
                    break;
                case R.id.nav_german:
                    setLocale("de");
                    break;


//                case R.id.nav_notifications:
//                    openFragment(WebViewFragment.newInstance("https://your.notifications.page"));
//                    break;
                case R.id.nav_help:
                    openFragment(WebViewFragment.newInstance("https://your.help.page"));
                    break;

            }
            drawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        });

        // Open fragment based on notification
        String url = getIntent().getStringExtra("url");
        if (savedInstanceState == null) {
            if (url != null && !url.isEmpty()) {
                bottomNav.setSelectedItemId(R.id.nav_home);
                openFragment(WebViewFragment.newInstance(url));
            } else {
                bottomNav.setSelectedItemId(R.id.nav_home); // Default tab
            }
        }
    }

    private void channelMapping() {
        List<Channel> channels = Arrays.asList(
                new Channel("akhbar-souria", R.id.channel_syria, 3),
                new Channel("iqtisad", R.id.channel_economy, 8),
                new Channel("akhbar", R.id.channel_general, 1),
                new Channel("mahalli", R.id.channel_local, 32),
                new Channel("maqalat", R.id.channel_articles, 6)
        );

        for (Channel channel : channels) {
            MenuItem item = navView.getMenu().findItem(channel.menuId);
            if (item != null && item.getActionView() != null) {
                SwitchCompat switchCompat = item.getActionView().findViewById(R.id.switch_channel);

                // Set initial checked state
                boolean isChecked = prefs.getBoolean(channel.key, false);
                switchCompat.setChecked(isChecked);

                switchCompat.setOnCheckedChangeListener((buttonView, isCheckedNow) -> {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(channel.key, isCheckedNow);


                    Set<String> selectedCategoryIds = new HashSet<>();
                    for (Channel ch : channels) {
                        boolean chState = ch.key.equals(channel.key) ? isCheckedNow : prefs.getBoolean(ch.key, false);
                        if (chState) {
                            selectedCategoryIds.add(String.valueOf(ch.categoryId));
                        }
                    }

                    editor.putString("last_toggled_categories", String.join(",", selectedCategoryIds));
                    editor.apply();

                    if (channelChangeListener != null) {
                        channelChangeListener.onChannelsChanged();
                    }

                    if (bottomNav.getSelectedItemId() != R.id.nav_home) {
                        bottomNav.setSelectedItemId(R.id.nav_home);
                    }
                });
            }
        }
    }
    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void scheduleNewsWorker() {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                NewsWorker.class, 15, TimeUnit.MINUTES
        ).build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                "news_check", ExistingPeriodicWorkPolicy.KEEP, request
        );
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "news_channel", "News Updates", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Restart activity to apply changes
        Intent intent = new Intent(this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void darkModeClick() {
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(darkMode);

// Set theme according to preference
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked).apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            recreate(); // Optional: for instant UI update
        });

    }

    public interface ChannelChangeListener {
        void onChannelsChanged();
    }

    private ChannelChangeListener channelChangeListener;

    public void setChannelChangeListener(ChannelChangeListener listener) {
        this.channelChangeListener = listener;
    }
}





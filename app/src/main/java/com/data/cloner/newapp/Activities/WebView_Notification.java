package com.data.cloner.newapp.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.data.cloner.newapp.Fragments.WebViewFragment;
import com.data.cloner.newapp.R;

public class WebView_Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view_notification);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        String url = getIntent().getStringExtra("url");
        Log.d("WebView_Notification", "OPening URL: " + url);

        if (savedInstanceState == null && url != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.web_fragment_container, WebViewFragment.newInstance(url))
                    .commit();
        }
    }
}

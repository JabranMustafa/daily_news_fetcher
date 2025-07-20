package com.data.cloner.newapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.data.cloner.newapp.Notification.NotificationScreen;
import com.data.cloner.newapp.R;
import com.data.cloner.newapp.utils.PermissionUtils;

public class ThirdScreen extends AppCompatActivity {
    Button previous,next;
    TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_third_screen);
        previous=findViewById(R.id.btnPrevious);
        next=findViewById(R.id.btnDone);
        skip=findViewById(R.id.tvSkip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        previous.setOnClickListener(v -> {
            Intent intent = new Intent(ThirdScreen.this, SecondScreen.class);
            startActivity(intent);
        });
        next.setOnClickListener(v -> {
            if (PermissionUtils.isNotificationPermissionGranted(ThirdScreen.this)) {
                // Notification already allowed → go to Home
                startActivity(new Intent(ThirdScreen.this, NewsActivity.class));
            } else {
                // Not allowed yet → show NotificationActivity
                startActivity(new Intent(ThirdScreen.this, NotificationScreen.class));
            }
        });
        skip.setOnClickListener(v -> {
            if (PermissionUtils.isNotificationPermissionGranted(ThirdScreen.this)) {
                // Notification already allowed → go to Home
                startActivity(new Intent(ThirdScreen.this, NewsActivity.class));
            } else {
                // Not allowed yet → show NotificationActivity
                startActivity(new Intent(ThirdScreen.this, NotificationScreen.class));
            }
        });
    }
}
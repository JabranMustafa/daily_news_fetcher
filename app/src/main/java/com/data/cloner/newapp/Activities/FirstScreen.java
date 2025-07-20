package com.data.cloner.newapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FirstScreen extends AppCompatActivity {
    Button btnNext;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_screen);

        btnNext = findViewById(R.id.btnfirstScreen);
        skip = findViewById(R.id.tvSkip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(FirstScreen.this, SecondScreen.class);
            startActivity(intent);
        });
        skip.setOnClickListener(v -> {
            if (PermissionUtils.isNotificationPermissionGranted(FirstScreen.this)) {
                // Notification already allowed → go to Home
                startActivity(new Intent(FirstScreen.this, NewsActivity.class));
            } else {
                // Not allowed yet → show NotificationActivity
                startActivity(new Intent(FirstScreen.this, NotificationScreen.class));
            }
//                Intent intent = new Intent(FirstScreen.this,NotificationScreen.class);
//                startActivity(intent);
        });
        printHashKey(this);

    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d("TAG", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("TAG", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("TAG", "printHashKey()", e);
        }
    }
}
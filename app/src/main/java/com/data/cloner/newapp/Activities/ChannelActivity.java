package com.data.cloner.newapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.data.cloner.newapp.R;

public class ChannelActivity extends AppCompatActivity {
    private boolean isSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        ConstraintLayout channelItem = findViewById(R.id.channel1);
        TextView channelText = findViewById(R.id.channelName);
        channelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected = !isSelected;

                // Toggle state
                if (isSelected) {
                    channelItem.setBackgroundColor(Color.parseColor("#FF5733")); // Change color
                } else {
                    channelItem.setBackgroundColor(Color.parseColor("#333333")); // Reset color
                }
            }
        });
    }
}
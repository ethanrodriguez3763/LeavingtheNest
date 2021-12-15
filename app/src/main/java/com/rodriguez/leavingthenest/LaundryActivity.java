package com.rodriguez.leavingthenest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class LaundryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);
        ImageView tidePods = findViewById(R.id.tidePodsImageView);
        tidePods.setImageResource(R.drawable.tidepods);
        ImageView gain = findViewById(R.id.gainImageView);
        gain.setImageResource(R.drawable.gain);
        ImageView downy = findViewById(R.id.downyImageView);
        downy.setImageResource(R.drawable.downey);
        ImageView basket = findViewById(R.id.basketImageView);
        basket.setImageResource(R.drawable.laundrybasket);
    }
}
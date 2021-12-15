package com.rodriguez.leavingthenest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        //Set up a back button
        Toolbar toolbar = findViewById(R.id.laundryToolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
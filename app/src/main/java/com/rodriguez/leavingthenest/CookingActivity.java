package com.rodriguez.leavingthenest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);



        ImageButton breakfastButton = findViewById(R.id.breakfastImageButton);
        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implicit intent example #1
                // start an activity that can handle and intent
                // for viewing a webpage
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // URI: uniform resource identifier
                Uri breakfastUri = Uri.parse("https://preppykitchen.com/avocado-toast/");
                intent.setData(breakfastUri);
                startActivity(intent);
            }
        });
        ImageButton lunchButton = findViewById(R.id.lunchImageButton);
        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implicit intent example #1
                // start an activity that can handle and intent
                // for viewing a webpage
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // URI: uniform resource identifier
                Uri lunchUri = Uri.parse("https://preppykitchen.com/walnut-strawberry-salad/");
                intent.setData(lunchUri);
                startActivity(intent);
            }
        });
        ImageButton dinnerButton = findViewById(R.id.dinnerImageButton);
        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implicit intent example #1
                // start an activity that can handle and intent
                // for viewing a webpage
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // URI: uniform resource identifier
                Uri dinnerUri = Uri.parse("https://preppykitchen.com/mac-and-cheese/");
                intent.setData(dinnerUri);
                startActivity(intent);
            }
        });
        ImageButton dessertButton = findViewById(R.id.dessertImageButton);
        dessertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implicit intent example #1
                // start an activity that can handle and intent
                // for viewing a webpage
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // URI: uniform resource identifier
                Uri dessertUri = Uri.parse("https://preppykitchen.com/banana-cream-pie/");
                intent.setData(dessertUri);
                startActivity(intent);
            }
        });
    }
}
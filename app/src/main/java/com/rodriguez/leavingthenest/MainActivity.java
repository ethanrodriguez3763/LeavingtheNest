package com.rodriguez.leavingthenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView appLogo = findViewById(R.id.logoImage);
        appLogo.setImageResource(R.drawable.applogo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.chatActivity:
                //Launch the Firebase Chat activity
                Intent intent = new Intent(MainActivity.this, FirebaseChatActivity.class);
                startActivity(intent); //Don't think we will need any information back from this so no need for launcher
                return true;
            case R.id.watchVideos:
                //Launch videos activity
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
package com.rodriguez.leavingthenest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    List<Section> sectionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView appLogo = findViewById(R.id.logoImage);
        appLogo.setImageResource(R.drawable.applogo);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                    }
                });
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
        Intent intent = null;
        switch(itemId) {
            case R.id.chatActivity:
                //Launch the Firebase Chat activity
                intent = new Intent(MainActivity.this, FirebaseChatActivity.class);
                startActivity(intent); //Don't think we will need any information back from this so no need for launcher
                return true;
            case R.id.watchVideos:
                //Launch Videos Activity
                intent = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(intent);
                return true;
            case R.id.todoActivity:
                //Launch ToDoList Activity
                intent = new Intent(MainActivity.this, ToDoListActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    //sets up places layout in the recyclerview
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView section;
            TextView description;

            //puts places layout into the recycler view
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                section = itemView.findViewById(R.id.sectionTextView);
                description = itemView.findViewById(R.id.descriptionTextView);

                itemView.setOnClickListener(this);

            }

            //updates MainActivity after changes are made
            public void updateView() {
                section.setText("Section");
                description.setText("Description");
            }

            //when a place is clicked this will take the user to PlacesDetailActivity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SectionActivity.class);
                launcher.launch(intent);
            }

            //the use can long lick on a video to delete it
            @Override
            public boolean onLongClick(View v) {
                return true;
            }

        }


        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.main_menu_layout, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            Section s = sectionList.get(position);
            //holder.updateView(s);
        }

        @Override
        public int getItemCount() {
            if (sectionList ==null){
                return 0;
            }
            return sectionList.size();
        }
    }
}
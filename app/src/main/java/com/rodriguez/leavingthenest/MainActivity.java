package com.rodriguez.leavingthenest;

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
    //sets up places layout in the recyclerview
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView text1;
            TextView text2;
            String placeID;

            //puts places layout into the recycler view
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                text1 = itemView.findViewById(R.id.textView6);
                text2 = itemView.findViewById(R.id.textView7);

                itemView.setOnClickListener(this);

            }

            //updates MainActivity after changes are made
            public void updateView() {
                text1.setText(place.getName() + " " + "("+ place.getRating()+ "/5 STARS)");
                text2.setText(place.getAddress());
            }

            //when a place is clicked this will take the user to PlacesDetailActivity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlacesDetailActivity.class);
                intent.putExtra("placeID", this.placeID);
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
                    .inflate(R.layout.places_layout, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

            Place p = placeList.get(position);
            holder.updateView(p);
        }

        @Override
        public int getItemCount() {
            if (placeList ==null){
                return 0;
            }
            return placeList.size();
        }
    }
}
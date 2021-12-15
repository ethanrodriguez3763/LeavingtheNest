package com.rodriguez.leavingthenest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity {

    static final String TAG = "ToDoTag";

    ActivityResultLauncher<Intent> launcher;
    TextView addItemsTextView;
    List<Integer> toDoItemIds = new ArrayList<>();
    List<Integer> incrementVals = new ArrayList<>();
    Integer incrementVal = 1;
    ToDoItemHelper helper;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Toolbar toolbar = findViewById(R.id.todoToolbar);
        setSupportActionBar(toolbar);
        //Set up a back button
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //Set up layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Set up adapter
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
        //Set up helper and incrementVals
        helper = new ToDoItemHelper(this);
        setIncrementVals();

        addItemsTextView = findViewById(R.id.addItemsMessage);
        if(incrementVals.size() > 0) {
            addItemsTextView.setVisibility(View.INVISIBLE);
        }

        //Set up launcher
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null) {
                                //int id = data.getIntExtra("Id", 0);
                                //Log.d(TAG, "ID: " + id);
                                String title = data.getStringExtra("Title");
                                Log.d(TAG, "Title: " + title);
                                String description = data.getStringExtra("Description");
                                int importance = data.getIntExtra("Importance", 0);
                                Boolean newItem = data.getBooleanExtra("New Item", false);
                                ToDoItem toDoItem = new ToDoItem(title, description, importance);

                                if(newItem) {
                                    helper.insertToDoItem(toDoItem);
                                    incrementVals.add(incrementVal);
                                    addItemsTextView.setVisibility(View.INVISIBLE);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    helper.updateToDoItemById(toDoItem);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

        FloatingActionButton fab = findViewById(R.id.addToDo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoListActivity.this, ToDoItemDetailActivity.class);
                intent.putExtra("New Item", true);
                launcher.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // need to get a menu inflater to inflate our main_menu.xml
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_todo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.deleteAll:
                //Show Alert Dialogue
                if(adapter.getItemCount() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ToDoListActivity.this);
                    builder.setTitle("DELETE ALL")
                            .setMessage("Do you want to delete all of the items in your To Do List?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Delete all
                                    incrementVal = 1 + helper.getAllToDoItems().get(adapter.getItemCount() - 1).getId();
                                    Log.d(TAG, "incrementVal: " + incrementVal);
                                    updateIncrementVals(0);
                                    toDoItemIds.clear();
                                    addItemsTextView.setVisibility(View.VISIBLE);
                                    helper.deleteAllToDoItems();
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    builder.show();
                } else {
                    Toast.makeText(ToDoListActivity.this, "No items to delete", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            int toDoItemId;
            TextView toDoItemTitle;
            TextView toDoItemDescription;
            ImageButton deleteToDoItem;
            int toDoItemImportance;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                toDoItemTitle = itemView.findViewById(R.id.toDoItemTitle);
                toDoItemDescription = itemView.findViewById(R.id.toDoItemDescription);
                deleteToDoItem = itemView.findViewById(R.id.toDoItemDelete);
                deleteToDoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Delete the item
                        helper.deleteToDoItem(toDoItemId);
                        incrementVal++;
                        Log.d(TAG, "Start Position: " + getAdapterPosition());
                        updateIncrementVals(getAdapterPosition());
                        if(getItemCount() == 0) {
                            addItemsTextView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                itemView.setOnClickListener(this);
            }

            public void updateView(ToDoItem toDoItem) {
                toDoItemId = toDoItem.getId();
                toDoItemTitle.setText(toDoItem.getTitle());
                toDoItemDescription.setText(toDoItem.getDescription());
                toDoItemImportance = toDoItem.getImportance();
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoListActivity.this, ToDoItemDetailActivity.class);
                intent.putExtra("Id", toDoItemId);
                intent.putExtra("Title", toDoItemTitle.getText());
                intent.putExtra("Description", toDoItemDescription.getText());
                intent.putExtra("Importance", toDoItemImportance);
                intent.putExtra("New Item", false);
                launcher.launch(intent);
            }
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ToDoListActivity.this)
                    .inflate(R.layout.card_view_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
            Log.d(TAG, "i: " + position + incrementVals.get(position));
            ToDoItem toDoItem = helper.getSelectToDoItem(position + incrementVals.get(position));
            if(toDoItem != null) {
                Log.d(TAG, "Item ID: " + toDoItem.getId());
                toDoItemIds.add(toDoItem.getId());
                holder.updateView(toDoItem);
            }
        }

        @Override
        public int getItemCount() {
            return helper.getAllToDoItems().size();
        }
    }

    /**
     * Method updates the incrementVals when an item is deleted
     *
     * @param startIdx
     */
    private void updateIncrementVals(int startIdx) {
        for(int i = startIdx; i < incrementVals.size() - 1; ++i) {
            incrementVals.set(i, incrementVal);
        }
        incrementVals.remove(incrementVals.size() - 1);
    }

    /**
     * Method sets the initial incrementVals whenever the app is loaded
     */
    private void setIncrementVals() {
        for(int i = 0; i < adapter.getItemCount(); ++i) {
            incrementVals.add(helper.getAllToDoItems().get(i).getId() - i);
        }
    }
}
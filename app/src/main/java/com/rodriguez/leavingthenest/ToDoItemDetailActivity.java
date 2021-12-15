package com.rodriguez.leavingthenest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ToDoItemDetailActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    Spinner spinner;
    Boolean newItem;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item_detail);

        title = findViewById(R.id.titleText);
        description = findViewById(R.id.descriptionBox);
        //Populate the spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Get intent
        Intent intent = getIntent();
        if(intent != null) {
            newItem = intent.getBooleanExtra("New Item", false);
            if(!newItem) {
                itemId = intent.getIntExtra("Id", 0);
                title.setText(intent.getStringExtra("Title"));
                switch(intent.getIntExtra("Importance", 2)) {
                    case 1:
                        spinner.setSelection(0);
                    case 2:
                        spinner.setSelection(1);
                    case 3:
                        spinner.setSelection(2);
                    default:
                        spinner.setSelection(0);
                }
                description.setText(intent.getStringExtra("Description"));
            } else {
                spinner.setSelection(1);
            }
        }

        //Set Save Button onCLick
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //Check user input a title
                try {
                    if(title.getText().toString().isEmpty()) {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(ToDoItemDetailActivity.this, "Title cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                intent.putExtra("Id", itemId); //Null if it is a new item
                intent.putExtra("Title", title.getText().toString());
                intent.putExtra("Description", description.getText().toString());
                int importance = 2;
                switch (spinner.getSelectedItem().toString()) {
                    case "Urgent":
                        importance = 3;
                    case "Important":
                        importance = 2;
                    case "Relaxed":
                        importance = 1;
                }
                intent.putExtra("Importance", importance);
                intent.putExtra("New Item", newItem);

                ToDoItemDetailActivity.this.setResult(Activity.RESULT_OK, intent);
                ToDoItemDetailActivity.this.finish();
            }
        });

        //Set Cancel Button onClick
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDoItemDetailActivity.this.setResult(Activity.RESULT_OK);
                ToDoItemDetailActivity.this.finish();
            }
        });


    }
}
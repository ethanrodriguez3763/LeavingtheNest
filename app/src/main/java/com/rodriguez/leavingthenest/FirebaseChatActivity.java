package com.rodriguez.leavingthenest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseChatActivity extends AppCompatActivity {
    static final String TAG = "FirebaseFunTag";

    ActivityResultLauncher<Intent> launcher;

    String userName = "Anonymous";
    List<ChatMessage> chatMessageList;
    CustomAdapter adapter;

    // firebase fields
    FirebaseDatabase mFirebaseDatabase;
    // we are going to add an object called messages
    DatabaseReference mMessagesDatabaseReference;
    ChildEventListener mMessagesChildEventListener;
    // firebase authentication fields
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Toast.makeText(FirebaseChatActivity.this, "You are now signed in", Toast.LENGTH_SHORT).show();
                        }
                        else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                            // they backed out of the sign in activity
                            // let's exit
                            finish();
                        }
                    }
                });

        //Set up a back button
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chatMessageList = new ArrayList<>();
//        chatMessageList.add(new ChatMessage());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        setupFirebase();
    }

    private void setupFirebase() {
        // initialize the firebase references
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase =
                FirebaseDatabase.getInstance();
        mMessagesDatabaseReference =
                mFirebaseDatabase.getReference()
                        .child("messages");
        mMessagesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // called for each message already in our db
                // called for each new message add to our db
                // dataSnapshot stores the ChatMessage
                Log.d(TAG, "onChildAdded: " + s);
                ChatMessage chatMessage =
                        dataSnapshot.getValue(ChatMessage.class);
                // add it to our list and notify our adapter
                chatMessageList.add(chatMessage);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        // server side setup
        // 1. enable authentication providers like
        // email or google or facebook etc.
        // today we will do email and google
        // 2. return the default values for db
        // read and write to be authenticated
        // client side setup
        // 3. declare a FirebaseAuth.AuthStateListener
        // listens for authentication events
        // signed in and signed out are our two states
        // 4. if the user is signed in...
        // let's get their user name
        // wire up our childeventlistener mMessagesChildEventListener
        // 5. if the user is not signed in...
        // start an activity using FirebaseUI to
        // log our user in
        // 6. wire up the AuthStateListener in onResume()
        // and detach it onPause()
        // 7. add support for the user logging out
        // with an options menu action

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // we have two auth states, signed in and signed out
                // get the get current user, if there is one
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    // step 4
                    setupUserSignedIn(user);
                } else {
                    // user is signed out
                    // step 5
                    // we need an intent
                    // the firebaseUI Github repo README.md
                    // we have used builders before in this class
                    // AlertDialog.Builder
                    // return instance to support chaining
                    Intent intent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    )
                            ).build();
                    launcher.launch(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        // attach the authstatelistener
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove it
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        chatMessageList.clear();
    }

    private void setupUserSignedIn(FirebaseUser user) {
        // get the user's name
        userName = user.getDisplayName();
        // listen for database changes with childeventlistener
        // wire it up!
        mMessagesDatabaseReference
                .addChildEventListener(mMessagesChildEventListener);
    }

    public void onSendButtonClick(View view) {
        // show a log message
        Log.d(TAG, "onSendButtonClick: ");
        // push up to "messages" whatever is
        // in the edittext
        EditText editText = (EditText)
                findViewById(R.id.editText);
        String currText = editText.getText().toString();
        if (currText.isEmpty()) {
            Toast.makeText(this, "Please enter a message first", Toast.LENGTH_SHORT).show();
        }
        else {
            // we have a message to send
            // create a ChatMessage object to push
            // to the database
            ChatMessage chatMessage = new
                    ChatMessage(userName,
                    currText);
            mMessagesDatabaseReference
                    .push()
                    .setValue(chatMessage);
            // warmup task #1
            editText.setText("");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_signout) {
            AuthUI.getInstance().signOut(this);
            chatMessageList.clear();
            adapter.notifyDataSetChanged();
            mMessagesDatabaseReference.removeEventListener(mMessagesChildEventListener);
        }

        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView text1;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
            }

            public void updateView(ChatMessage c) {
                text1.setText(c.toString());
            }
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(FirebaseChatActivity.this)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            holder.updateView(chatMessageList.get(position));
        }

        @Override
        public int getItemCount() {
            return chatMessageList.size();
        }
    }
}
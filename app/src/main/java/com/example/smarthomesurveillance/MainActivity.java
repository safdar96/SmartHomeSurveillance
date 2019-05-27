package com.example.smarthomesurveillance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.smarthomesurveillance.adapters.LoggingEventListAdapter;
import com.example.smarthomesurveillance.data.LoggingEvent;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener, LoggingEventListAdapter.LogDeleteBtnListener {


    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public static DatabaseReference mDatabase;
    private RecyclerView mLogEventListRv;
    private LoggingEventListAdapter mAdapter;
    private ChildEventListener logEventListener;
    ArrayList<LoggingEvent> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mLogEventListRv = findViewById(R.id.log_event_rv);
        mLogEventListRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mLogEventListRv.setLayoutManager(layoutManager);

        mList = new ArrayList<>();
        mAdapter = new LoggingEventListAdapter();
        mAdapter.setList(mList);
        mAdapter.setListener(this);
        mLogEventListRv.setAdapter(mAdapter);
        attachDatabaseReadListener();
    }

    public void attachDatabaseReadListener() {
        if (logEventListener == null) {
            logEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    LoggingEvent loggingEvent = dataSnapshot.getValue(LoggingEvent.class);
                    loggingEvent.setId(dataSnapshot.getKey());
                    mAdapter.add(loggingEvent);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mDatabase.child("Logging Event").addChildEventListener(logEventListener);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_entries) {
            mDatabase.child("Logging Event").setValue(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.time_logging_event) {
            // Handle the camera action
            Intent intent = new Intent(this, ApplianceStatusActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onPause() {
        mAuth.removeAuthStateListener(this);
        super.onPause();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        mUser = firebaseAuth.getCurrentUser();
        if (mUser == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(
                            Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                    .build(), RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                mUser = mAuth.getCurrentUser();
            } else {
                Toast.makeText(this, "Sign in not successful!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onDelete(LoggingEvent loggingEvent) {
        mDatabase.child("Logging Event").child(loggingEvent.getId()).setValue(null);
    }
}

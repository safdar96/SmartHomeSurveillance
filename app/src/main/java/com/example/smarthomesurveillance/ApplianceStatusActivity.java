package com.example.smarthomesurveillance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.smarthomesurveillance.adapters.ApplianceStatusListAdapter;
import com.example.smarthomesurveillance.data.AppliancesStatus;
import com.example.smarthomesurveillance.data.LoggingEvent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class ApplianceStatusActivity extends AppCompatActivity implements ApplianceStatusListAdapter.OnAppStatusClickListener {

    RecyclerView AppStatusRv;
    ApplianceStatusListAdapter mAdapter;
    ChildEventListener appStatusListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_status);
        AppStatusRv = findViewById(R.id.app_status_rv);
        AppStatusRv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<AppliancesStatus> list = new ArrayList<>();
        mAdapter = new ApplianceStatusListAdapter();
        mAdapter.setList(list);
        mAdapter.setListener(this);
        AppStatusRv.setAdapter(mAdapter);

        attachDBReadListener();
    }

    public void attachDBReadListener() {
        if (appStatusListener == null) {
            appStatusListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    AppliancesStatus appStatus = dataSnapshot.getValue(AppliancesStatus.class);
                    appStatus.setApplianceName(dataSnapshot.getKey());
                    mAdapter.add(appStatus);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    mAdapter.changeStatus(dataSnapshot.getKey());
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
        }
        MainActivity.mDatabase.child("Appliances Status").addChildEventListener(appStatusListener);
    }

    @Override
    public void onItemClick(AppliancesStatus appStatus, String status) {
        MainActivity.mDatabase.child("Appliances Status").child(appStatus.getApplianceName()).child("Status").setValue(status);
        Toast.makeText(this, appStatus.getApplianceName() + " will be turned " + status, Toast.LENGTH_SHORT).show();
    }
}

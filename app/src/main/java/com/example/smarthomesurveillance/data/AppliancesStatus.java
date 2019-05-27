package com.example.smarthomesurveillance.data;

import android.util.Log;

public class AppliancesStatus {
    private int mId;
    private String mApplianceName;
    private long LastUpdation;
    private String Status;

    public AppliancesStatus() {}
    public AppliancesStatus(long lastUpdation, String status) {
        LastUpdation = lastUpdation;
        Status = status;
    }

    public void setApplianceName(String mApplianceName) {
        this.mApplianceName = mApplianceName;
    }

    public String getApplianceName() {
        return mApplianceName;
    }

    public long getLastUpdation() {
        return LastUpdation;
    }

    public String getStatus() {
        return Status;
    }

    public void setLastUpdation(long lastUpdation) {
        LastUpdation = lastUpdation;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return mId;
    }
}

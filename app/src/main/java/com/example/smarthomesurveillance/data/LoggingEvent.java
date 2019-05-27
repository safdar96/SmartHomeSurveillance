package com.example.smarthomesurveillance.data;

import android.util.Log;

public class LoggingEvent{
    private String mId;
    private String Authorized;
    private String EntryOrExit;
    private String Object;
    private long Timestamp;

    public LoggingEvent() {}
    public LoggingEvent(String authorized, String entryOrExit, String object, long timestamp) {
        Authorized = authorized;
        EntryOrExit = entryOrExit;
        Object = object;
        Timestamp = timestamp;
        Log.v("Saf",  authorized + " " + entryOrExit + " " + object + " " + timestamp);
    }

    public String getAuthorized() {
        return Authorized;
    }

    public String getEntryOrExit() {
        return EntryOrExit;
    }
    public String getObject() {
        return Object;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setAuthorized(String authorized) {
        Authorized = authorized;
    }

    public void setEntryOrExit(String entryOrExit) {
        EntryOrExit = entryOrExit;
    }

    public void setObject(String object) {
        Object = object;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getId() {
        return mId;
    }
}

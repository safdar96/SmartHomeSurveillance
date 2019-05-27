package com.example.smarthomesurveillance.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarthomesurveillance.R;
import com.example.smarthomesurveillance.data.LoggingEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LoggingEventListAdapter extends RecyclerView.Adapter<LoggingEventListAdapter.LoggingEventListViewHolder> {

    ArrayList<LoggingEvent> mList;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEEEE");
    LogDeleteBtnListener mListener;

    @NonNull
    @Override
    public LoggingEventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_logg_event_list, parent, false);
        return new LoggingEventListViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull LoggingEventListViewHolder holder, int position) {
        holder.bind(position);
    }

    public void setList(ArrayList<LoggingEvent> list) {
        mList = list;
    }

    public void setListener(LogDeleteBtnListener listener) {
        mListener = listener;
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(LoggingEvent loggingEvent) {
        mList.add(loggingEvent);
        notifyDataSetChanged();
    }

    class LoggingEventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mObjectName, mTime, mDate, mDay, mEntryOrExit;
        ImageView mDeleteBtn;
        Context mContext;
        View mView;
        LoggingEvent loggingEvent;

        public LoggingEventListViewHolder(View itemView, Context context) {
            super(itemView);
            mView = itemView;
            mObjectName = mView.findViewById(R.id.obj_name);
            mTime = mView.findViewById(R.id.time_log_val);
            mDate = mView.findViewById(R.id.date);
            mDay = mView.findViewById(R.id.day);
            mDeleteBtn = mView.findViewById(R.id.delete_log_event);
            mEntryOrExit = mView.findViewById(R.id.entry_or_exit);
            mContext = context;
        }

        public void bind(int pos) {
            loggingEvent = mList.get(pos);
            long time = loggingEvent.getTimestamp() * 1000;
            mTime.setText(timeFormat.format(time));
            mDay.setText(dayFormat.format(time));
            mDate.setText(dateFormat.format(time));
            mDeleteBtn.setTag(pos);
            mDeleteBtn.setOnClickListener(this);
            if (loggingEvent.getEntryOrExit().equalsIgnoreCase("entry")) {
                mEntryOrExit.setText(loggingEvent.getEntryOrExit().toUpperCase());
                mEntryOrExit.setTextColor(mContext.getResources().getColor(R.color.colorGreen, null));
            } else {
                mEntryOrExit.setText(loggingEvent.getEntryOrExit().toUpperCase());
                mEntryOrExit.setTextColor(mContext.getResources().getColor(R.color.colorRed, null));
            }
            mView.setTag(loggingEvent.getId());
            mObjectName.setText(loggingEvent.getObject());
        }

        @Override
        public void onClick(View v) {
            int pos = (int)v.getTag();
            mListener.onDelete(mList.get(pos));
            mList.remove(pos);
            notifyDataSetChanged();
        }
    }

    public interface LogDeleteBtnListener {
        public void onDelete(LoggingEvent loggingEvent);
    }
}

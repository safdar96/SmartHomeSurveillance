package com.example.smarthomesurveillance.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smarthomesurveillance.ApplianceStatusActivity;
import com.example.smarthomesurveillance.R;
import com.example.smarthomesurveillance.data.AppliancesStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ApplianceStatusListAdapter extends RecyclerView.Adapter<ApplianceStatusListAdapter.ApplianceStatusViewHolder> {

    ArrayList<AppliancesStatus> mList;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    OnAppStatusClickListener itemClickListener;

    @NonNull
    @Override
    public ApplianceStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_app_status, parent, false);
        return new ApplianceStatusViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ApplianceStatusViewHolder holder, int position) {
        holder.bind(position);
    }


    public void changeStatus(String key) {
        for (AppliancesStatus a : mList) {
            if (a.getApplianceName().equalsIgnoreCase(key)) {
                if (a.getStatus().equalsIgnoreCase("on")) {
                    a.setStatus("off");
                } else {
                    a.setStatus("on");
                }
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setList(ArrayList<AppliancesStatus> list) {
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(AppliancesStatus appStatus) {
        mList.add(appStatus);
        notifyDataSetChanged();
    }

    public void setListener(OnAppStatusClickListener listener) {
        itemClickListener = listener;
    }

    class ApplianceStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mApplianceName, mTime, mStatus, mDate;
        Context mContext;
        View mView;
        AppliancesStatus appliancesStatus;

        public ApplianceStatusViewHolder(View itemView, Context context) {
            super(itemView);
            mView = itemView;
            mApplianceName = mView.findViewById(R.id.obj_name);
            mStatus = mView.findViewById(R.id.app_status);
            mTime = mView.findViewById(R.id.last_updation_at);
            mDate = mView.findViewById(R.id.date);
            mContext = context;
        }

        public void bind(int pos) {
            appliancesStatus = mList.get(pos);
            long time = appliancesStatus.getLastUpdation() * 1000;
            mView.setTag(pos);
            mStatus.setTag(pos);
            mStatus.setOnClickListener(this);
            mApplianceName.setText(appliancesStatus.getApplianceName());
            mStatus.setText(appliancesStatus.getStatus().toUpperCase());
            mTime.setText(timeFormat.format(time));
            mDate.setText(dateFormat.format(time));
        }

        @Override
        public void onClick(View v) {
            String status;
            int pos = (int) v.getTag();
            if (mList.get(pos).getStatus().equalsIgnoreCase("on")) {
                status = "off";
            } else {
                status = "on";
            }
            itemClickListener.onItemClick(mList.get(pos), status);
        }
    }

    public interface OnAppStatusClickListener {
        public void onItemClick(AppliancesStatus appStatus, String status);
    }
}

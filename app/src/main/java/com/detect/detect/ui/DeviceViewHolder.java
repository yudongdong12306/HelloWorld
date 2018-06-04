package com.detect.detect.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.detect.detect.R;

public class DeviceViewHolder extends RecyclerView.ViewHolder {
    public TextView device_name_tv;

    public DeviceViewHolder(View itemView) {
        super(itemView);
        device_name_tv = itemView.findViewById(R.id.device_name_tv);
    }
}

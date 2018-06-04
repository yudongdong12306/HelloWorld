package com.detect.detect.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detect.detect.R;

import java.util.List;

import retrofit2.http.PUT;

public class MyAdapter extends RecyclerView.Adapter<DeviceViewHolder> {
    private final Context context;
    private List<String> deviceArr;
    private ItemClick itemClick;

    public MyAdapter(Context context, List<String> deviceArr) {
        this.context = context;
        this.deviceArr = deviceArr;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_device_item, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        if (deviceArr == null || deviceArr.size() == 0) {
            return;
        }
        final String device = deviceArr.get(position);
        if (TextUtils.isEmpty(device)) {
            return;
        }
        holder.device_name_tv.setText(device);
        holder.device_name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null) {
                    itemClick.onItemClick(device);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return deviceArr == null ? 0 : deviceArr.size();
    }

    public void setOnClick(ItemClick itemClick) {
        this.itemClick = itemClick;

    }

    public interface ItemClick {
        void onItemClick(String device);
    }
}

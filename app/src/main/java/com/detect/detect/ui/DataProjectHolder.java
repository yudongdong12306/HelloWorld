package com.detect.detect.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.detect.detect.R;

public class DataProjectHolder extends RecyclerView.ViewHolder {
    public TextView itemProjectNameTv;

    public DataProjectHolder(View itemView) {
        super(itemView);
        itemProjectNameTv = itemView.findViewById(R.id.item_project_name1_tv);
    }
}

package com.detect.detect.ui;

import android.view.View;
import android.widget.TextView;

import com.detect.detect.R;

public class ProjectNameNormalDataViewHolder extends ProjectNameBaseViewHolder {
    public TextView projectNameTv;

    public ProjectNameNormalDataViewHolder(View itemView) {
        super(itemView);
        projectNameTv = itemView.findViewById(R.id.item_project_name_tv);
    }
}

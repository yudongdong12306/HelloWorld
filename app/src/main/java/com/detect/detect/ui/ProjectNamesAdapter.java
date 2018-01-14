package com.detect.detect.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.detect.detect.R;

import java.util.List;

/**
 * Created by yu on 18/1/14.
 */

class ProjectNamesAdapter extends BaseAdapter {
    private Context context;
    private List<String> mAllProjectNames;

    public ProjectNamesAdapter(Context context, List<String> allProjectNames) {
        this.context = context;
        this.mAllProjectNames = allProjectNames;
    }

    @Override
    public int getCount() {
        return mAllProjectNames == null ? 0 : mAllProjectNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllProjectNames == null ? null : mAllProjectNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_project_lv_item, null);
        }
        if (mAllProjectNames != null && mAllProjectNames.size() > 0) {
            ViewHolder holder = ViewHolder.getHolder(convertView);
            holder.projectNameIteTv.setText(mAllProjectNames.get(position));
        }
        return convertView;
    }

    /**
     * 控件管理类
     */
    static class ViewHolder {
        TextView projectNameIteTv;

        private ViewHolder(View convertView) {
            projectNameIteTv = convertView.findViewById(R.id.project_name_item_tv);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder homeHolder = (ViewHolder) convertView.getTag();
            if (homeHolder == null) {
                homeHolder = new ViewHolder(convertView);
                convertView.setTag(homeHolder);
            }
            return homeHolder;
        }

    }
}

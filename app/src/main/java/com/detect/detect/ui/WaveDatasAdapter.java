package com.detect.detect.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.detect.detect.R;

import java.util.List;

/**
 * Created by yu on 18/1/14.
 */

class WaveDatasAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> mWaveDataList;

    public WaveDatasAdapter(Context context, List<Integer> waveDataList) {
        this.context = context;
        this.mWaveDataList = waveDataList;
    }

    @Override
    public int getCount() {
        return mWaveDataList == null ? 0 : mWaveDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWaveDataList == null ? null : mWaveDataList.get(position);
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
        if (mWaveDataList != null && mWaveDataList.size() > 0) {
            ViewHolder holder = ViewHolder.getHolder(convertView);
            holder.projectNameIteTv.setText(mWaveDataList.get(position)+"");
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

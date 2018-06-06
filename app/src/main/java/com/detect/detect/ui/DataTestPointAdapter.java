package com.detect.detect.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.TestPoint;

import java.nio.file.Path;
import java.util.List;

public class DataTestPointAdapter extends RecyclerView.Adapter<DataProjectHolder> {
    private Context context;
    private List<TestPoint> testPointList;
    private ItemClickListener listener;

    public DataTestPointAdapter(Context context, List<TestPoint> testPointList) {
        this.context = context;
        this.testPointList = testPointList;
    }

    @Override
    public DataProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data_project_name, parent, false);
        return new DataProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataProjectHolder holder, int position) {

        final TestPoint testPoint = testPointList.get(position);
        if (testPoint != null) {
            String buildSerialNum = testPoint.getBuildSerialNum();
            if (!TextUtils.isEmpty(buildSerialNum)) {
                holder.itemProjectNameTv.setText(buildSerialNum);
                holder.itemProjectNameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClick(testPoint);
                        }
                    }
                });
            }
        }
    }

    public void setOnItemClick(ItemClickListener listener) {
        this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return testPointList.size();
    }

    public interface ItemClickListener {
        void onItemClick(TestPoint testPoint);
    }
}

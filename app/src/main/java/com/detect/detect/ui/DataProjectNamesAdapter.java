package com.detect.detect.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.Project;

import java.nio.file.Path;
import java.util.List;

public class DataProjectNamesAdapter extends RecyclerView.Adapter<DataProjectHolder> {
    private Context context;
    private List<Project> projectInfoList;
    private ItemClickListener listener;

    public DataProjectNamesAdapter(Context context, List<Project> projectInfoList) {
        this.context = context;
        this.projectInfoList = projectInfoList;
    }

    @Override
    public DataProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data_project_name, parent, false);
        return new DataProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataProjectHolder holder, int position) {
        final Project project = projectInfoList.get(position);
        if (project != null) {
            String projectName = project.getProjectName();
            if (!TextUtils.isEmpty(projectName)) {
                holder.itemProjectNameTv.setText(projectName);
                holder.itemProjectNameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClick(project);
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
        return projectInfoList.size();
    }

    public interface ItemClickListener {
        void onItemClick(Project project);
    }
}

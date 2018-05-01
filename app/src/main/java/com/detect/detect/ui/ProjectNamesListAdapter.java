package com.detect.detect.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.ProjectInfo;

import java.util.List;

public class ProjectNamesListAdapter extends RecyclerView.Adapter<ProjectNameBaseViewHolder> {
    private static final int NORMAL_DATA = 1;
    private static final int EMPTY_DATA = 2;
    private Context mContext;
    private List<ProjectInfo> mProjectList;
    private OnItemClickListener listener;

    public ProjectNamesListAdapter(Context context, List<ProjectInfo> projectList) {
        this.mContext = context;
        this.mProjectList = projectList;
    }

    @Override
    public ProjectNameBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProjectNameBaseViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_project_name_rv_item, parent, false);
        if (viewType == NORMAL_DATA) {
            viewHolder = new ProjectNameNormalDataViewHolder(view);
        } else {
            viewHolder = new ProjectNameEmptyViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectNameBaseViewHolder holder, final int position) {
        if (holder instanceof ProjectNameNormalDataViewHolder) {
            ProjectNameNormalDataViewHolder holder1 = (ProjectNameNormalDataViewHolder) holder;
            holder1.projectNameTv.setText(mProjectList.get(position).getProjectName());
            holder1.projectNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(v, mProjectList.get(position));
                    }
                }
            }); holder1.projectNameTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onLongItemClick(v, mProjectList.get(position));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mProjectList == null || mProjectList.size() == 0) {
            return 1;
        } else {
            return mProjectList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mProjectList == null || mProjectList.size() == 0) {
            return EMPTY_DATA;
        } else {
            return NORMAL_DATA;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(View v, ProjectInfo project);

        void onLongItemClick(View v, ProjectInfo projectInfo);
    }
}

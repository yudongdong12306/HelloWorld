package com.detect.detect.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.utils.ToastUtils;

import java.util.List;

public class ProjectDataAdapter extends BaseExpandableListAdapter {
    private IProjectTestPointClickCallback mPointClickCallback;
    private Context context;
    private List<String> groupData;
    private List<List<Integer>> childData;

    public ProjectDataAdapter(Context context, List<String> groupData, List<List<Integer>> childData, IProjectTestPointClickCallback pointClickCallback) {
        this.context = context;
        this.groupData = groupData;
        this.childData = childData;
        this.mPointClickCallback = pointClickCallback;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    //获取分组个数
    @Override
    public int getGroupCount() {
        int ret = 0;
        if (groupData != null) {
            ret = groupData.size();
        }
        return ret;
    }

    //获取groupPosition分组，子列表数量
    @Override
    public int getChildrenCount(int groupPosition) {
        int ret = 0;
        if (childData != null) {
            ret = childData.get(groupPosition).size();
        }
        return ret;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_expandablelistview, null);
            holder = new GroupViewHolder();
            holder.projectName = convertView.findViewById(R.id.item_project_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        String groupData = this.groupData.get(groupPosition);
        holder.projectName.setText(groupData);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_child_expandablelistview, null);
            holder = new ChildViewHolder();
            holder.testPoint = convertView.findViewById(R.id.item_test_point_tv);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        final int childData = this.childData.get(groupPosition).get(childPosition);
        holder.testPoint.setText(childData + "");
        holder.testPoint.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String projectName = ProjectDataAdapter.this.groupData.get(groupPosition);
                if (mPointClickCallback != null) {
                    mPointClickCallback.onTestPointSelected(projectName, childData);
                }
                return true;
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    class GroupViewHolder {
        TextView projectName;
    }

    class ChildViewHolder {
        TextView testPoint;
    }

}
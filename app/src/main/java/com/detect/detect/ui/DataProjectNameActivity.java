package com.detect.detect.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.constant.GlobalContants;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.ProjectDataManager;
import com.detect.detect.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class DataProjectNameActivity extends BaseActivity {
    @BindView(R.id.data_project_name_rc)
    RecyclerView dataProjecRc;
    @BindView(R.id.common_title_tv)
    TextView titleNameTv;
    @BindView(R.id.common_back_ll)
    LinearLayout backBt;

    @Override
    protected void initData() {
        titleNameTv.setText("选择工程");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_project_name;
    }

    @OnClick({R.id.common_back_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll:
                finish();
                break;
        }
    }

    @Override
    public void initView() {
        List<Project> allProjects = ProjectDataManager.getInstance().getAllProjects();
        if (allProjects == null) {
            allProjects = new ArrayList<>();
        }
        dataProjecRc.setLayoutManager(new LinearLayoutManager(this));
        DataProjectNamesAdapter namesAdapter = new DataProjectNamesAdapter(this, allProjects);
        dataProjecRc.setAdapter(namesAdapter);
        namesAdapter.setOnItemClick(new DataProjectNamesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Project project) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalContants.SKIP_DATA_PROJECT_BUNDLE, project);
                UIUtils.intentActivity(DataSelectPointActivity.class, bundle, DataProjectNameActivity.this);
            }
        });
    }
}

package com.detect.detect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.constant.CommonConstant;
import com.detect.detect.constant.GlobalContants;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DataSelectPointActivity extends BaseActivity {
    @BindView(R.id.data_project_point_rc)
    RecyclerView dataPointRc;
    @BindView(R.id.common_title_tv)
    TextView titleNameTv;
    @BindView(R.id.common_back_ll)
    LinearLayout backBt;

    @Override
    protected void initData() {
        titleNameTv.setText("选择检测点");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_point_name;
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

        List<TestPoint> testPointList = null;
        Intent intent = getIntent();
        if (intent != null) {
            Project project = (Project) intent.getSerializableExtra(GlobalContants.SKIP_DATA_PROJECT_BUNDLE);
            if (project != null) {
                testPointList = project.getTestPointList();
            }
        }
        if (testPointList == null) {
            testPointList = new ArrayList<>();
        }
//        List<Project> allProjects = ProjectDataManager.getInstance().getAllProjects();
//        if (allProjects == null) {
//            allProjects = new ArrayList<>();
//        }
        dataPointRc.setLayoutManager(new LinearLayoutManager(this));
        DataTestPointAdapter testPointAdapter = new DataTestPointAdapter(this, testPointList);
        dataPointRc.setAdapter(testPointAdapter);
        testPointAdapter.setOnItemClick(new DataTestPointAdapter.ItemClickListener() {
            @Override
            public void onItemClick(TestPoint testPoint) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalContants.SKIP_DATA_POINT_BUNDLE, testPoint);
                UIUtils.intentActivity(DataPointDetailActivity.class, bundle, DataSelectPointActivity.this);
            }
        });
    }
}

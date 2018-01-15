package com.detect.detect.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.ProjectInfoSP;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.utils.PicDisplayUtils;
import com.detect.detect.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/8.
 */

public class DataManagerActivity extends BaseActivity implements IProjectTestPointClickCallback {
    @BindView(R.id.project_data_lv)
    ExpandableListView projectDataLv;
    @BindView(R.id.project_name_tv)
    TextView projectNameTv;
    @BindView(R.id.test_point_data_bt)
    Button testPointDataBt;
    @BindView(R.id.test_point_pic_bt)
    Button testPointPicBt;
    @BindView(R.id.data_fl)
    FrameLayout dataFl;
    @BindView(R.id.back_bt)
    Button backBt;
    @BindView(R.id.add_pic_bt)
    Button addPicBt;
    @BindView(R.id.out_put_bt)
    Button outPutBt;
    private String mProjectName;
    private int mBuildSerialNum;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_manager;
    }


    @Override
    public void initView() {
        List<Project> allProjects = ProjectInfoSP.getInstance().getAllProjects();
        if (allProjects == null || allProjects.size() == 0) {
            return;
        }
        List<String> projectNameList = new ArrayList<>();
        List<Integer> testPointList = new ArrayList<>();
        List<List<Integer>> allTestPointList = new ArrayList<>();

        for (Project project : allProjects) {
            if (project != null) {
                projectNameList.add(project.getProjectName());
                List<TestPoint> testPoints = project.getTestPointList();
                for (TestPoint testPoint : testPoints) {
                    if (testPoint != null) {
                        testPointList.add(testPoint.getBuildSerialNum());
                    }
                }
                allTestPointList.add(testPointList);
            }
        }

        projectDataLv.setAdapter(new ProjectDataAdapter(this, projectNameList, allTestPointList, this));
    }

    @OnClick({R.id.project_name_tv, R.id.test_point_data_bt, R.id.test_point_pic_bt, R.id.back_bt, R.id.add_pic_bt, R.id.out_put_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.project_name_tv:
                break;
            case R.id.test_point_data_bt:
                break;
            case R.id.test_point_pic_bt:
                showPic();
                break;
            case R.id.back_bt:
                finish();
                break;
            case R.id.add_pic_bt:
                addPic();
                break;
            case R.id.out_put_bt:
                outputData();
                break;
        }
    }

    private void outputData() {
        ToastUtils.showToast("导出数据成功!");
    }

    private void addPic() {

    }

    private void showPic() {
        if (TextUtils.isEmpty(mProjectName) || mBuildSerialNum < 1) {
            return;
        }
        String testPointPicPath = ProjectInfoSP.getInstance().getTestPointPicPath(mProjectName
                , mBuildSerialNum);
        if (TextUtils.isEmpty(testPointPicPath)) {
            return;
        }
        Bitmap bitmap = PicDisplayUtils.decodeSampledBitmap(testPointPicPath, 2000, 720);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        dataFl.addView(imageView);

    }

    @Override
    public void onTestPointSelected(String projectName, int buildSerialNum) {
        this.mProjectName = projectName;
        this.mBuildSerialNum = buildSerialNum;
        ToastUtils.showToast("选中了: " + projectName + "工程" + buildSerialNum + "节点");
        projectNameTv.setText("工程" + projectName + "构件序号" + buildSerialNum);
    }
}

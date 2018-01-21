package com.detect.detect.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.ProjectDataManagerDB;
import com.detect.detect.shared_preferences.ProjectInfoSP;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.utils.PicDisplayUtils;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.widgets.PersonalPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/8.
 */

public class DataManagerActivity extends BaseActivity implements IProjectTestPointClickCallback, ITakePhoto {
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
    private PersonalPopupWindow popupWindow;
    private DataManagerPresenter mPresenter;

    @Override
    protected void initData() {
        mPresenter = new DataManagerPresenter(this);
        initPopupView();
    }

    @Override
    public void initPopupView() {
        popupWindow = new PersonalPopupWindow(this);
        popupWindow.setOnItemClickListener(new PersonalPopupWindow.OnItemClickListener() {
            @Override
            public void onCaremaClicked() {
                mPresenter.btnCameraClicked();
            }

            @Override
            public void onPhotoClicked() {
                mPresenter.btnPhotoClicked();
            }

            @Override
            public void onCancelClicked() {
                mPresenter.btnCancelClicked();
            }
        });
    }


    @Override
    public void showHead(Bitmap bitmap) {
//        civHead.setImageBitmap(bitmap);
    }

    @Override
    public void showPopupView() {
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_start_detect, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    @Override
    public void dismissPopupView() {
        popupWindow.dismiss();
    }

    @Override
    public boolean popupIsShowing() {
        return popupWindow.isShowing();
    }

    @Override
    public void gotoHeadSettingActivity(String path) {
        if (TextUtils.isEmpty(path)) {
            ToastUtils.showToast("获取图片失败,请重试!");
            return;
        }
        ToastUtils.showToast("添加图片成功,图片路径: " + path);
    }

    @Override
    public void gotoSystemPhoto(int requestCode) {
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), requestCode);
    }

    @Override
    public void gotoSystemCamera(File tempFile, int requestCode) {
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            //            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //            Uri contentUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + "" +
            //                    ".fileProvider", tempFile);
            //            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mPresenter.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_manager;
    }


    @Override
    public void initView() {
//        List<Project> allProjects = ProjectInfoSP.getInstance().getAllProjects();
        List<Project> allProjects = ProjectDataManagerDB.getInstance().getAllProjects();
        if (allProjects == null || allProjects.size() == 0) {
            return;
        }
        List<String> projectNameList = new ArrayList<>();
        List<List<Integer>> allTestPointList = new ArrayList<>();
        for (Project project : allProjects) {
            if (project != null) {
                projectNameList.add(project.getProjectName());
                List<TestPoint> testPoints = project.getTestPointList();
                List<Integer> testPointList = new ArrayList<>();
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
        if (TextUtils.isEmpty(mProjectName) || mBuildSerialNum < 1) {
            ToastUtils.showToast("未选择构件,无法添加图片!");
            return;
        }
        showPopupView();
    }

    private void showPic() {
        if (TextUtils.isEmpty(mProjectName) || mBuildSerialNum < 1) {
            return;
        }
//        String testPointPicPath = ProjectInfoSP.getInstance().getTestPointPicPath(mProjectName
//                , mBuildSerialNum);
        String testPointPicPath = ProjectDataManagerDB.getInstance().getTestPointPicPath(mProjectName
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

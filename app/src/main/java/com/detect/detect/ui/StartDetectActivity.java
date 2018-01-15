package com.detect.detect.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.LatestTestPointSP;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.ProjectInfoSP;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.widgets.PersonalPopupWindow;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/7.
 */

public class StartDetectActivity extends BaseActivity implements ITakePhoto {
    private static final String TAG = "StartDetect";
    private static final int REQUEST_CODE_PROJECT_DETAIL = 100;
    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.project_info_et)
    EditText projectInfoEt;
    @BindView(R.id.build_serial_num_et)
    EditText buildSerialNumEt;
    @BindView(R.id.coordinate_info_et)
    EditText coordinateInfoEt;
    @BindView(R.id.detect_time_et)
    EditText detectTimeEt;
    @BindView(R.id.take_photo_bt)
    Button takePhotoBt;
    @BindView(R.id.confirm_bt)
    Button confirmBt;

    private PersonalPopupWindow popupWindow;
    private StartDetectPresenter mPresenter;
    private TestPoint testPointInsert;
    private String picPath;

    @Override
    protected void initData() {
        mPresenter = new StartDetectPresenter(this);
        initPopupView();
        //每次构建序号有变化,请清空拍照路径
        buildSerialNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                picPath = null;
                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }
                try {
                    int num = Integer.parseInt(s.toString());
                    if (num < 1) {
                        ToastUtils.showToast("构建序号最小为1!");
                    }
                }catch (Exception ignored){
                    buildSerialNumEt.setText("");
                }

            }
        });
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
        this.picPath = path;
        ToastUtils.showToast("获取照片成功,图片路径: " + path);
//        if (uri == null) {
//            return;
//        }
//        Intent intent = new Intent();
//        intent.setClass(this, HeadSettingActivity.class);
//        intent.setData(uri);
//        startActivity(intent);
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
        if (requestCode == REQUEST_CODE_PROJECT_DETAIL) {
            Log.d(TAG, "onActivityResult: ");
            switch (resultCode) {
//                case ProjectInfoDetailSetActivity.RESULT_CODE_START_DETECT_NEW_BUILD:
//                    testPointNew = (TestPoint) intent.getSerializableExtra(ProjectInfoDetailSetActivity.NEW_BUILD_TEST_POINT);
//                    projectInfoEt.setText(testPointNew.getProjectName());
//                    int maxBuildSerialNum1 = ProjectInfoSP.getInstance().getMaxBuildSerialNum(testPointNew.getProjectName());
//                    buildSerialNumEt.setText(maxBuildSerialNum1 + 1 + "");
//                    Log.d(TAG, "onActivityResult: 1");
//                    break;
                case ProjectInfoDetailSetActivity.RESULT_CODE_START_DETECT_INSERT:
                    testPointInsert = (TestPoint) intent.getSerializableExtra(ProjectInfoDetailSetActivity.INSERT_TEST_POINT);
                    projectInfoEt.setText(testPointInsert.getProjectName());
                    int maxBuildSerialNum = ProjectInfoSP.getInstance().getMaxBuildSerialNum(testPointInsert.getProjectName());
                    buildSerialNumEt.setText(maxBuildSerialNum + 1 + "");
                    Log.d(TAG, "onActivityResult: 2");
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_start_detect;
    }

    @Override
    public void initView() {
        TestPoint latestDetectPoint = LatestTestPointSP.getInstance().getLatestDetectPoint();
        if (latestDetectPoint == null) {
            return;
        }
        //取上次的,对界面进行恢复,仅仅是恢复,方便用户修改再用
        buildSerialNumEt.setText(latestDetectPoint.getBuildSerialNum() + "");
        coordinateInfoEt.setText(latestDetectPoint.getCoordinateInfo());
        detectTimeEt.setText(latestDetectPoint.getDetectTime() + "");
        projectInfoEt.setText(latestDetectPoint.getProjectName());
        int maxBuildSerialNum = ProjectInfoSP.getInstance().getMaxBuildSerialNum(latestDetectPoint.getProjectName());
        buildSerialNumEt.setText(maxBuildSerialNum + 1 + "");
        //对详情页面数据进行初始化
        testPointInsert = new TestPoint();
        testPointInsert.setProjectName(latestDetectPoint.getProjectName());
        testPointInsert.setConstructionOrganization(latestDetectPoint.getConstructionOrganization());
        testPointInsert.setFillerType(latestDetectPoint.getFillerType());
        testPointInsert.setInstrumentNumber(latestDetectPoint.getInstrumentNumber());
        testPointInsert.setDetectPerson(latestDetectPoint.getDetectPerson());
    }


    @OnClick({R.id.common_back_ll, R.id.take_photo_bt, R.id.confirm_bt, R.id.project_info_et})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.take_photo_bt:
                showPopupView();
                break;
            case R.id.confirm_bt:
                TestPoint testPointData = getTestPointData();
                if (testPointData == null) {
                    return;
                }
                LatestTestPointSP.getInstance().setLatestDetectPoint(testPointData);
                ProjectInfoSP.getInstance().insertTestPoint(testPointInsert.getProjectName(), testPointInsert);
                startActivity(new Intent(this, DetectActivity.class));
                break;
            case R.id.project_info_et:
                //重置
                testPointInsert = null;
                startActivityForResult(new Intent(this, ProjectInfoDetailSetActivity.class), REQUEST_CODE_PROJECT_DETAIL);
                break;
        }
    }

    private TestPoint getTestPointData() {
        String buildSerialNum = buildSerialNumEt.getText().toString().trim();
        String coordinateInfo = coordinateInfoEt.getText().toString().trim();
        String detectTime = detectTimeEt.getText().toString().trim();
        if (TextUtils.isEmpty(buildSerialNum)) {
            ToastUtils.showToast("构建序号不能为空!");
            return null;
        }
        if (TextUtils.isEmpty(coordinateInfo)) {
            ToastUtils.showToast("坐标信息不能为空!");
            return null;
        }
        if (TextUtils.isEmpty(detectTime)) {
            ToastUtils.showToast("测试时间不能为空!");
            return null;
        }
        if (TextUtils.isEmpty(picPath)) {
            ToastUtils.showToast("没有拍照!");
            return null;
        }
        if (testPointInsert != null) {
            testPointInsert.setBuildSerialNum(Integer.parseInt(buildSerialNum));
            testPointInsert.setCoordinateInfo(coordinateInfo);
            testPointInsert.setDetectTime(Integer.parseInt(detectTime));
            testPointInsert.setPicPath(picPath);
            return testPointInsert;
        }
        ToastUtils.showToast("工程信息不能为空!");
        return null;
    }
}


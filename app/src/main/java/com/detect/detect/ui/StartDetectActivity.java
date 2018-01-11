package com.detect.detect.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.detect.detect.widgets.PersonalPopupWindow;
import com.detect.detect.R;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/7.
 */

public class StartDetectActivity extends BaseActivity implements ITakePhoto {
    @BindView(R.id.project_info_et)
    EditText projectInfoEt;
    @BindView(R.id.build_serial_num_tv)
    EditText buildSerialNumTv;
    @BindView(R.id.coordinate_info_et)
    EditText coordinateInfoEt;
    @BindView(R.id.detect_time_et)
    EditText detectTimeEt;
    @BindView(R.id.back_tv)
    TextView backTv;
    @BindView(R.id.take_photo_tv)
    TextView takePhotoTv;
    @BindView(R.id.print_tv)
    TextView printTv;
    private PersonalPopupWindow popupWindow;
    private StartDetectPresenter mPresenter;

    @Override
    protected void initData() {
        mPresenter = new StartDetectPresenter(this);
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

    }

    @OnClick({R.id.back_tv, R.id.take_photo_tv, R.id.print_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_tv:
                finish();
                break;
            case R.id.take_photo_tv:
                showPopupView();
                break;
            case R.id.print_tv:
                break;
        }
    }
}

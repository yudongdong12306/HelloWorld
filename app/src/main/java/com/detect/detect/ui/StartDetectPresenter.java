package com.detect.detect.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.detect.detect.utils.AppUtils;
import com.detect.detect.utils.FileUtils;
import com.detect.detect.utils.MD5Utils;

import java.io.File;

import static com.detect.detect.constant.HeadConstant.HEAD_IMAGE_NAME;


/**
 * Created by dongdong.yu on 18/1/11.
 */


public class StartDetectPresenter {
    private static final String TAG = "StartDetectPresenter";
    //请求相机
    private static final int REQUEST_CAMERA = 100;
    //请求相册
    private static final int REQUEST_PHOTO = 101;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    private final ITakePhoto mIView;

    private File tempFile;

    public StartDetectPresenter(ITakePhoto mIView) {
        this.mIView = mIView;
    }


//    @Override
//    public void btnHeadClicked() {
//        mIView.showPopupView();
//    }

    public void btnCameraClicked() {
        //创建拍照存储的图片文件
        tempFile = new File(FileUtils.checkDirPath(Environment.getExternalStorageDirectory()
                .getPath() + "/image/"), MD5Utils.getMD5(HEAD_IMAGE_NAME) + System
                .currentTimeMillis() + ".jpg");
        //权限判断
        if (ContextCompat.checkSelfPermission(mIView.getActivity(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mIView.getActivity(), new String[]{Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            Log.d(TAG, "btnCameraClicked: 11");
        } else {
            Log.d(TAG, "btnCameraClicked: 22");
            //跳转到调用系统相机
            mIView.gotoSystemCamera(tempFile, REQUEST_CAMERA);
        }
        if (mIView.popupIsShowing())
            mIView.dismissPopupView();
    }

    public void btnPhotoClicked() {
        //权限判断
        if (ContextCompat.checkSelfPermission(mIView.getActivity(), Manifest.permission
                .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请READ_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mIView.getActivity(), new String[]{Manifest
                            .permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            //跳转到相册
            mIView.gotoSystemPhoto(REQUEST_PHOTO);
        }
        if (mIView.popupIsShowing())
            mIView.dismissPopupView();
    }

    public void btnCancelClicked() {
        if (mIView.popupIsShowing())
            mIView.dismissPopupView();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                if (mIView != null)
                    mIView.gotoSystemCamera(tempFile, REQUEST_CAMERA);
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                if (mIView != null)
                    mIView.gotoSystemPhoto(REQUEST_PHOTO);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAMERA: //调用系统相机返回
                if (resultCode == Activity.RESULT_OK) {
//                    mIView.gotoHeadSettingActivity(Uri.fromFile(tempFile));
                    mIView.gotoHeadSettingActivity(tempFile.getAbsolutePath());
                }
                break;
            case REQUEST_PHOTO:  //调用系统相册返回
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        mIView.gotoHeadSettingActivity(uri.getPath());
                    }
                }
                break;
        }
    }

    public void onStart() {
        //此处实际应用中替换成服务器拉取图片
        Uri headUri = Uri.fromFile(new File(mIView.getActivity().getCacheDir(), HEAD_IMAGE_NAME +
                ".jpg"));
        if (headUri != null) {
            String cropImagePath = FileUtils.getRealFilePathFromUri(AppUtils.getContext(), headUri);
            Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
            if (bitMap != null)
                mIView.showHead(bitMap);
        }
    }
}

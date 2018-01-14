package com.detect.detect.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;


import com.detect.detect.R;
import com.detect.detect.helper.RxHelper;


import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by dongdong.yu on 2018/1/9.
 */
public class FlashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "FlashActivity";
    private int mTime = 3;
    private static final int RC_CAMERA_PERM = 123;

    @Override
    protected void initData() {

    }

    private boolean hasPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_flash;
    }

    @Override
    public void initView() {
        checkPermissions();
    }

    /**
     * 检测权限
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermission()) {
            Log.d(TAG, "checkPermissions: 没有获取权限");
            requestPermissions();
        } else {

            initCountDown();
            Log.d(TAG, "checkPermissions: 获取了权限");
        }
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        EasyPermissions.requestPermissions(
                this,
                getString(R.string.request_tips),
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void initCountDown() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(2)//计时次数
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return mTime - aLong;// 3-0 3-2 3-1
                    }
                })
                .compose(RxHelper.<Long>rxSchedulerHelper())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long value) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        startActivity(new Intent(FlashActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted: ");
        if (hasPermission()) {
            initCountDown();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (!hasPermission()) {
            new AppSettingsDialog.Builder(this).build().show();

        }
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Log.d(TAG, "onActivityResult: ");
            if (!hasPermission()) {
                restartAPP();
            } else {
                initCountDown();
            }
        }
    }

    /**
     * 重启应用
     */
    private void restartAPP() {
        Log.d(TAG, "restartAPP: 重启应用");
        // 重启当前的Activity,System.exit(0) 必须退出整个程序再重启
        System.exit(0);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}

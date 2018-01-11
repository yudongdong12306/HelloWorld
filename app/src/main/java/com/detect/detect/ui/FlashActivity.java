package com.detect.detect.ui;

import android.Manifest;
import android.content.Intent;


import com.detect.detect.R;
import com.detect.detect.helper.RxHelper;
import com.detect.detect.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dongdong.yu on 2018/1/9.
 */
public class FlashActivity extends BaseActivity {

    private static final String TAG = "RxPermission";
    private int mTime = 3;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_flash;
    }

    @Override
    public void initView() {
        requestPermissions();
    }


    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(FlashActivity.this);
        //请求权限全部结果
        rxPermission.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            ToastUtils.showToast("App未能获取全部需要的相关权限，部分功能可能不能正常使用.");
                        }
                        //不管是否获取全部权限，进入主页面
                        initCountDown();
                    }
                });
        //分别请求权限
        //        rxPermission.requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
        //                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //                Manifest.permission.READ_CALENDAR,
        //                Manifest.permission.READ_CALL_LOG,
        //                Manifest.permission.READ_CONTACTS,
        //                Manifest.permission.READ_PHONE_STATE,
        //                Manifest.permission.READ_SMS,
        //                Manifest.permission.RECORD_AUDIO,
        //                Manifest.permission.CAMERA,
        //                Manifest.permission.CALL_PHONE,
        //                Manifest.permission.SEND_SMS)
        //注：魅族pro6s-7.0-flyme6权限没有像类似6.0以上手机一样正常的提示dialog获取运行时权限，而是直接默认给了权限。魅族pro6s动态获取权限不会回调下面的方法
        //        rxPermission.requestEach(
        //                Manifest.permission.CAMERA,
        //                Manifest.permission.READ_PHONE_STATE,
        //                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //                Manifest.permission.READ_EXTERNAL_STORAGE,
        //                Manifest.permission.ACCESS_COARSE_LOCATION)
        //                .subscribe(new Consumer<Permission>() {
        //                    @Override
        //                    public void accept(Permission permission) throws Exception {
        //                        if (permission.granted) {
        //                            // 用户已经同意该权限
        //                            Log.d(TAG, permission.name + " is granted.");
        //                        } else if (permission.shouldShowRequestPermissionRationale) {
        //                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
        //                            Log.d(TAG, permission.name + " is denied. More info should
        // be provided.");
        //                        } else {
        //                            // 用户拒绝了该权限，并且选中『不再询问』
        //                            Log.d(TAG, permission.name + " is denied.");
        //                        }
        //                    }
        //                });
    }

    private void initCountDown() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(3)//计时次数
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
}

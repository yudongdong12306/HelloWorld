package com.detect.detect.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.detect.detect.R;
import com.detect.detect.shared_preferences.CommonSP;
import com.detect.detect.utils.UIUtils;
import com.gsh.dialoglibrary.RaiingAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by dongdong.yu on 2018/1/7.
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.start_detect_tv)
    TextView startDetectTv;
    @BindView(R.id.data_manage_tv)
    TextView dataManageTv;
    @BindView(R.id.system_set_tv)
    TextView systemSetTv;
    @BindView(R.id.about_us_tv)
    TextView aboutUsTv;
    private static final int LOCATION_TAG = 1011;
    private static final String TAG = "MainActivity";

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

//    @OnClick({R.id.about_us_tv, R.id.start_detect_tv, R.id.data_manage_tv, R.id.system_set_tv})
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.about_us_tv:
//                startActivity(new Intent(this, AboutUsActivity.class));
//                break;
//            case R.id.start_detect_tv:
//                startActivity(new Intent(this, NewTestPointActivity.class));
//                break;
//            case R.id.data_manage_tv:
//                startActivity(new Intent(this, DataManagerActivity.class));
//                break;
//            case R.id.system_set_tv:
//                startActivity(new Intent(this, SystemSetActivity.class));
//                break;
//        }
//    }

    @Override
    public void initView() {
    }


    @OnClick({R.id.start_detect_tv, R.id.data_manage_tv, R.id.system_set_tv, R.id.about_us_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about_us_tv:
                UIUtils.intentActivity(AboutUsActivity.class, null, this);
                break;
            case R.id.start_detect_tv:
                UIUtils.intentActivity(ShowHistoryActivity.class, null, this);
                break;
            case R.id.data_manage_tv:
                UIUtils.intentActivity(DataProjectNameActivity.class, null, this);
                break;
            case R.id.system_set_tv:
                UIUtils.intentActivity(SystemSettingsActivity.class, null, this);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //检测定位权限
        String[] location = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            EasyPermissions.requestPermissions(this, "开启定位权限,否则无法定位", LOCATION_TAG, location);
        } else {
            initLocation();
        }

    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2 * 1000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private void initLocation() {
        //初始化client
        AMapLocationClient locationClient = new AMapLocationClient(this);
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.d(TAG, "onLocationChanged() called with: aMapLocation = [" + aMapLocation + "]");
            double longitude = aMapLocation.getLongitude();
            double latitude = aMapLocation.getLatitude();
            CommonSP.getInstance().setLatitude(latitude);
            CommonSP.getInstance().setLongitude(longitude);
        }
    };

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (!hasPermission()) {
            new AppSettingsDialog.Builder(this).build().show();

        }
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (!hasPermission()) {
                restartAPP();
            } else {
                initLocation();
            }
        }
    }

    /**
     * 重启应用
     */
    private void restartAPP() {
        // 重启当前的Activity,System.exit(0) 必须退出整个程序再重启
        System.exit(0);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}

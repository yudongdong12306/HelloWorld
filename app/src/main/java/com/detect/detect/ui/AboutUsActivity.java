package com.detect.detect.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/9.
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.settings_wifi_et)
    EditText settingsWifiEt;
    @BindView(R.id.settings_bluetooth_tv)
    EditText settingsBluetoothTv;
    @BindView(R.id.instrument_number_et)
    EditText instrumentNumberEt;
    @BindView(R.id.settings_language_et)
    EditText settingsLanguageEt;
    @BindView(R.id.check_update_bt)
    Button checkUpdateBt;

    @Override
    protected void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.common_back_ll, R.id.check_update_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.check_update_bt:
                ToastUtils.showToast("检测更新！");
                break;
        }
    }
}

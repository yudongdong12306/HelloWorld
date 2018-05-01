package com.detect.detect.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yu on 18/1/13.
 */

public class SystemSettingsActivity extends BaseActivity {
    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.settings_wifi_et)
    EditText settings_wifi_et;
    @BindView(R.id.settings_bluetooth_et)
    EditText settings_bluetooth_et;
    @BindView(R.id.instrument_number_et)
    EditText instrument_number_et;
    @BindView(R.id.params_1_et)
    EditText params_1_et;
    @BindView(R.id.params_2_et)
    EditText params_2_et;
    @BindView(R.id.settings_language_et)
    EditText settings_language_et;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_system_settings;
    }

    @Override
    public void initView() {

    }


    @OnClick(R.id.common_back_ll)
    public void onViewClicked() {
        finish();
    }
}

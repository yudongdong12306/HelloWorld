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
    @BindView(R.id.project_name_et)
    EditText projectNameEt;
    @BindView(R.id.project_nam_t)
    EditText projectNamT;
    @BindView(R.id.projec_nam_et)
    EditText projecNamEt;
    @BindView(R.id.project_nam_et)
    EditText projectNamEt;

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

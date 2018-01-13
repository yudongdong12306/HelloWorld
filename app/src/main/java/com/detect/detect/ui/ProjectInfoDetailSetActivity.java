package com.detect.detect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ProjectInfoDetailSetActivity extends BaseActivity {
    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.project_name_et)
    EditText projectNameEt;
    @BindView(R.id.construction_organization_tv)
    EditText constructionOrganizationTv;
    @BindView(R.id.filler_type_et)
    EditText fillerTypeEt;
    @BindView(R.id.instrument_number_et)
    EditText instrumentNumberEt;
    @BindView(R.id.detect_person_et)
    EditText detectPersonEt;
    @BindView(R.id.newly_build_bt)
    Button newlyBuildBt;
    @BindView(R.id.confirm_bt)
    Button confirmBt;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_project_info_detail_set;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.newly_build_bt, R.id.common_back_ll, R.id.confirm_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.newly_build_bt:
                finish();
                break;
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.confirm_bt:
                startActivity(new Intent(this, DetectActivity.class));
                break;
        }
    }
}

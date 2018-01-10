package com.detect.detect;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by dongdong.yu on 2018/1/7.
 */
public class MainActivity extends BaseActivity  {
    @BindView(R.id.about_us_tv)
    TextView aboutUsTv;
    @BindView(R.id.start_detect_tv)
    TextView startDetectTv;

    @BindView(R.id.data_manage_tv)
    TextView dataManager;
    @BindView(R.id.system_set_tv)
    TextView systemSetTv;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.about_us_tv, R.id.start_detect_tv, R.id.data_manage_tv, R.id.system_set_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_us_tv:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.start_detect_tv:
                startActivity(new Intent(this, StartDetectActivity.class));
                break;
            case R.id.data_manage_tv:
                startActivity(new Intent(this, DataManagerActivity.class));
                break;
            case R.id.system_set_tv:
                startActivity(new Intent(this, SystemSetActivity.class));
                break;
        }
    }

    @Override
    public void initView() {

    }
}

package com.detect.detect.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.Constant;
import com.bigkoo.pickerview.YMDPickerView;
import com.detect.detect.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongdong.yu on 2018/1/7.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.start_detect_tv)
    TextView startDetectTv;
    @BindView(R.id.data_manage_tv)
    TextView dataManageTv;
    @BindView(R.id.system_set_tv)
    TextView systemSetTv;
    @BindView(R.id.about_us_tv)
    TextView aboutUsTv;
//    @BindView(R.id.about_us_tv)
//    TextView aboutUsTv;
//    @BindView(R.id.start_detect_tv)
//    TextView startDetectTv;
//
//    @BindView(R.id.data_manage_tv)
//    TextView dataManager;
//    @BindView(R.id.system_set_tv)
//    TextView systemSetTv;

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
//                startActivity(new Intent(this, StartDetectActivity.class));
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
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.start_detect_tv:
                startActivity(new Intent(this, MainActivity1.class));
                break;
            case R.id.data_manage_tv:
                startActivity(new Intent(this, DataManagerActivity.class));
                break;
            case R.id.system_set_tv:
                startActivity(new Intent(this, SystemSettingsActivity.class));
                break;
        }
    }


}

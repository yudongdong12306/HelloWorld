package com.detect.detect.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.constant.SkipActivityConstant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yu on 18/1/13.
 */

public class DetectActivity extends BaseActivity {


    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.build_serial_num_et)
    EditText buildSerialNumEt;
    @BindView(R.id.rr_1)
    LinearLayout rr1;
    @BindView(R.id.detect_data_s1_et)
    EditText detectDataS1Et;
    @BindView(R.id.detect_state_tv)
    TextView detectStateTv;
    @BindView(R.id.rr_2)
    LinearLayout rr2;
    @BindView(R.id.detect_data_s2_et)
    EditText detectDataS2Et;
    @BindView(R.id.rr_3)
    LinearLayout rr3;
    @BindView(R.id.detect_data_s3_et)
    EditText detectDataS3Et;
    @BindView(R.id.rr_4)
    LinearLayout rr4;
    @BindView(R.id.cancel_bt)
    Button cancelBt;
    private String mProjectName;
    private int mBuildSerialNum;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detect;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(SkipActivityConstant.DETECT_PROJECT_NAME)) {
                mProjectName = intent.getStringExtra(SkipActivityConstant.DETECT_PROJECT_NAME);
            }
            if (intent.hasExtra(SkipActivityConstant.DETECT_TEST_POINT_SERIAL_BUILD_NUM)) {
                mBuildSerialNum = intent.getIntExtra(SkipActivityConstant.DETECT_TEST_POINT_SERIAL_BUILD_NUM, -1);
                buildSerialNumEt.setText(mBuildSerialNum + "");
            }
        }
    }

    @OnClick({R.id.common_back_ll, R.id.cancel_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.cancel_bt:
                startActivity(new Intent(this, DetectFinishedActivity.class));
                break;
        }
    }
}

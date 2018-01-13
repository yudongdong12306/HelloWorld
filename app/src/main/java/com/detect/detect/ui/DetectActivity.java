package com.detect.detect.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.detect.detect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yu on 18/1/13.
 */

public class DetectActivity extends BaseActivity {
    @BindView(R.id.build_serial_num_et)
    EditText buildSerialNumEt;
    @BindView(R.id.rr_1)
    LinearLayout rr1;
    @BindView(R.id.detect_data_s1_et)
    EditText detectDataS1Et;
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
    @BindView(R.id.confirm_bt)
    Button confirmBt;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detect;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.confirm_bt)
    public void onViewClicked() {
    }
}

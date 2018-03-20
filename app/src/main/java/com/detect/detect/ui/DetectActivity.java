package com.detect.detect.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.constant.SkipActivityConstant;
import com.detect.detect.socket.ChatMess;
import com.detect.detect.socket.MyRecParse;
import com.detect.detect.socket.RecData;
import com.tjstudy.tcplib.RequestCallback;
import com.tjstudy.tcplib.ResponseCallback;
import com.tjstudy.tcplib.TCPClient;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yu on 18/1/13.
 */

public class DetectActivity extends BaseActivity {
    private static final String TAG = "DetectActivity";

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
    private static final String IP = "10.10.100.254";
    private static final int PORT = 8899;
    private TCPClient tcpClient;
    private byte[] heart;
    private byte[] startbyte;
    private byte[]  heartqq, confirm;

    @Override
    protected void initData() {
        heart = new byte[31];
        for (int i = 0; i < heart.length; i++) {
            heart[i] = 0x00;
        }
        heart[1] = 0x08;
        heart[29] = 0x08;

        startbyte = new byte[31];
        for (int i = 0; i < startbyte.length; i++) {
            startbyte[i] = 0x00;
        }
        startbyte[0] = 0x01;
        startbyte[1] = 0x01;
        startbyte[29] = 0x02;
        initNet();

        tcpClient.request(/*sendMess.getBytes()*/startbyte, 8000, new RequestCallback() {
            @Override
            public void onTimeout() {
                Log.e(TAG, "onTimeout:请求超时，稍后重试 ,关闭连接 ");
                TCPClient.closeTcp(IP, PORT);
            }

            @Override
            public void onFail(Throwable throwable) {
                handlerError(throwable);
            }
        }, responseCallback);
    }

    private void initNet() {
        tcpClient = TCPClient.build()
                .server(IP, PORT)
                .breath(/*"heart".getBytes()*/heart, 6 * 1000)
                .connTimeout(10 * 1000);
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

    private void handlerError(Throwable throwable) {
        Log.e(TAG, "handlerError: 网络访问失败:" + throwable.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TCPClient.closeTcp(IP, PORT);
    }

    private ResponseCallback responseCallback = new ResponseCallback() {
        @Override
        public void onRec() {
            MyRecParse myRecParse = new MyRecParse();
            List<RecData> dataList = myRecParse.parse();
            Log.d(TAG, "onRec: dataList: " + dataList.toString());
            if (dataList.size() > 0) {
                for (RecData recData :
                        dataList) {
                    Log.d(TAG, "onRec: recData: " + recData);
                }
            }
        }

        @Override
        public void onFail(Throwable throwable) {
            handlerError(throwable);
        }
    };

}

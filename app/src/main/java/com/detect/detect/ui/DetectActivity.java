package com.detect.detect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.detect.detect.R;
import com.detect.detect.constant.SkipActivityConstant;
import com.detect.detect.shared_preferences.ProjectDataManager;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.socket.HexUtils;
import com.detect.detect.utils.ToastUtils;
import com.tjstudy.tcplib.RequestCallback;
import com.tjstudy.tcplib.ResponseCallback;
import com.tjstudy.tcplib.TCPClient;
import com.tjstudy.tcplib.utils.DigitalUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
    //    @BindView(R.id.rr_1)
//    LinearLayout rr1;
    @BindView(R.id.s1_et)
    EditText detectDataS1Et;
    @BindView(R.id.detect_state_tv)
    TextView detectStateTv;
    //    @BindView(R.id.rr_2)
//    LinearLayout rr2;
    @BindView(R.id.s2_et)
    EditText detectDataS2Et;
    //    @BindView(R.id.rr_3)
//    LinearLayout rr3;
    @BindView(R.id.s3_et)
    EditText detectDataS3Et;
    //    @BindView(R.id.rr_4)
//    LinearLayout rr4;
    @BindView(R.id.cancel_bt)
    Button cancelBt;
    private String mProjectName;
    private int mBuildSerialNum;
    private static final String IP = "10.10.100.254";
    private static final int PORT = 8899;
    private TCPClient tcpClient;
    private byte[] heartCommand, confirmCommand, startCommand;
    private TestPoint mTestPoint;

    @Override
    protected void initData() {
        initCommond();
        initNet();
        sendCommand(startCommand);
    }

    private void initCommond() {
        heartCommand = new byte[31];
        heartCommand[1] = 0x08;
        heartCommand[29] = 0x08;

        confirmCommand = new byte[31];
        confirmCommand[1] = 0x05;
        confirmCommand[29] = 0x05;

        startCommand = new byte[31];
        startCommand[0] = 0x01;
        startCommand[1] = 0x01;
        startCommand[29] = 0x02;
    }

    private void sendCommand(byte[] Command) {
        tcpClient.request(Command, 8000, new RequestCallback() {
            @Override
            public void onTimeout() {
                Log.e(TAG, "onTimeout:请求超时，稍后重试 ,关闭连接 ");
                TCPClient.closeTcp(IP, PORT);
                //
                detectStateTv.setText("请求超时，稍后重试");
            }

            @Override
            public void onFail(Throwable throwable) {
                handlerError(throwable);
                detectStateTv.setText("网络访问失败，稍后重试");
            }
        }, responseCallback);
    }

    private void initNet() {
        tcpClient = TCPClient.build()
                .server(IP, PORT)
                .breath(heartCommand, 6 * 1000)
                .connTimeout(10 * 1000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detect;
    }

    @Override
    public void initView() {
        commonTitleTv.setText("检测");
        ToastUtils.showToast("将开始三次撞击测试");
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(SkipActivityConstant.DETECT_TEST_POINT_TEST_POINT)) {
                mTestPoint = (TestPoint) intent.getSerializableExtra(SkipActivityConstant.DETECT_TEST_POINT_TEST_POINT);
                buildSerialNumEt.setText(mTestPoint.getBuildSerialNum());
            }
        }
    }

    @OnClick({R.id.common_back_ll, R.id.cancel_bt, R.id.detect_state_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll:
                finish();
                break;
            case R.id.cancel_bt:
//                startActivity(new Intent(this, DetectFinishedActivity.class));
                break;
            case R.id.detect_state_tv:
                sendCommand(confirmCommand);
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
        if (waveDataList != null) {
            waveDataList.clear();
            waveDataList = null;
        }
        if (waveData != null) {
            waveData = null;
        }
    }

    private WaveData waveData = new WaveData();
    private ArrayList<int[]> waveDataList = new ArrayList<>();
    private ResponseCallback responseCallback = new ResponseCallback() {
        @Override
        public void onRec(byte[] receiveData) {
            if (receiveData == null) {
                return;
            }
            if ((receiveData.length == 31) && (receiveData[1] == 0x08) && (receiveData[29] == 0x08)) {
                //过滤掉心跳数据
                return;
            }
            if (receiveData[0] == 1 && receiveData[1] == 7 && receiveData.length > 31) {
                //结束标志数据
                byte[] data = new byte[receiveData.length - 31];
                System.arraycopy(receiveData, 31, data, 0, data.length);
                String dataEndStr = null;
                try {
                    dataEndStr = new String(data, "gbk");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onRec: 返回的结束部分数据为: " + dataEndStr);
                if (waveDataList.size() >= 3) {
                    //全部测试完成
                    Log.d(TAG, "onRec: 全部波形数据接收完成");
                    Intent intent = new Intent(DetectActivity.this, DetectFinishedActivity.class);
                    intent.putExtra("WAVE_DATA", waveDataList);
                    startActivity(intent);
                    String waveListStr = JSON.toJSONString(waveDataList);
                    //保存数据到数据库
                    String str1 = detectDataS1Et.getText().toString();
                    String str2 = detectDataS2Et.getText().toString();
                    String str3 = detectDataS3Et.getText().toString();
                    if (!TextUtils.isEmpty(str1)
                            && !TextUtils.isEmpty(str2)
                            && !TextUtils.isEmpty(str3)) {
                        mTestPoint.setWaveListStr(waveListStr);
                        mTestPoint.setSubsidence(str1.trim().concat("_").concat(str2.trim()).concat("_").concat(str3.trim()));
                        ProjectDataManager.getInstance().insertTestPoint(mTestPoint.getProjectUUID(), mTestPoint);
                    } else {
                        detectStateTv.setText("获取沉陷值失败,请重试");
                    }

                }
                return;
            }
            Log.d(TAG, "onRec: reveiveData: " + HexUtils.byteToString(receiveData));

            if (waveData.isHasFirstDataArr && waveData.waveArrHasDataSize < 2033) {
                //当本次获取的数据添加后超过2033时,只取缺少的那部分数据.
                if (waveData.waveArrHasDataSize + receiveData.length > 2033) {
                    System.arraycopy(receiveData, 0, waveData.waveDataArr, waveData.waveArrHasDataSize, 2033 - waveData.waveArrHasDataSize);
                } else {
                    System.arraycopy(receiveData, 0, waveData.waveDataArr, waveData.waveArrHasDataSize, receiveData.length);
                }
                waveData.waveArrHasDataSize += receiveData.length;
                if (waveData.waveArrHasDataSize == 2033) {
                    int m = waveDataList.size() + 1;
                    detectStateTv.setText("接收测试点 " + m + " 数据成功");
                    //发出确认命令
                    sendCommand(confirmCommand);
                    //重置波形数据实例
                    waveData.isHasFirstDataArr = false;
                    waveData.waveArrHasDataSize = 0;
                    byte[] realData = new byte[2000];
                    System.arraycopy(waveData.waveDataArr, 32, realData, 0, 2000);
                    int[] finalDataArr = handleRealData(realData);
                    waveDataList.add(finalDataArr);
                    Log.d(TAG, "onRec: 波形数据接收完毕: waveData.waveArrHasDataSize: " + 2033 + "waveData.waveDataArr[31]: " + waveData.waveDataArr[31]);
                    Log.d(TAG, "onRec: 最后获取到的波形数据为: " + Arrays.toString(finalDataArr));
                }
                return;
            }
            if (receiveData[0] == 1 && receiveData[1] == 6 && receiveData.length > 31) {
                detectStateTv.setText("获取工程信息成功");
                //开始数据,参数部分.
                //todo 报文和数据校验
                byte[] dataParams = new byte[receiveData.length - 31];
                System.arraycopy(receiveData, 31, dataParams, 0, dataParams.length);
                String dataStr = null;
                try {
                    dataStr = new String(dataParams, "gbk");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onRec: 返回的参数部分数据为: " + dataStr);
                //显示界面信息
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(dataStr);
                if (jsonObject.containsKey("沉陷值1")) {
                    Float s1 = jsonObject.getFloat("沉陷值1");
                    detectDataS1Et.setText(String.valueOf(s1));
                }
                if (jsonObject.containsKey("沉陷值2")) {
                    Float s2 = jsonObject.getFloat("沉陷值2");
                    detectDataS2Et.setText(String.valueOf(s2));
                }
                if (jsonObject.containsKey("沉陷值3")) {
                    Float s3 = jsonObject.getFloat("沉陷值3");
                    detectDataS3Et.setText(String.valueOf(s3));
                }
                //发出确认命令
                sendCommand(confirmCommand);
                //
                detectStateTv.setText("等待接收设备数据...");
                //请用户开启下锤数据
            } else if (receiveData[0] == 2 && receiveData[1] == 6 && (receiveData.length == 1000 || receiveData.length == 1033)) {
                //获取到了波形数据,而且是第一次波形数据,保存下来.
                //重置波形数据实例
                waveData.waveArrHasDataSize = 0;
                waveData.isHasFirstDataArr = true;
                System.arraycopy(receiveData, 0, waveData.waveDataArr, 0, receiveData.length);
                waveData.waveArrHasDataSize = receiveData.length;
                int count = waveDataList.size() + 1;
                detectStateTv.setText("正在接收测试点" + count + "数据");
            }
        }

        @Override
        public void onConnectSuccess() {
//            detectStateTv.setText("请求数据成功");
        }

        @Override
        public void onFail(Throwable throwable) {
            handlerError(throwable);
        }
    };

    private int[] handleRealData(byte[] realData) {
        int[] finalDataArr = new int[1000];
        for (int i = 0; i < realData.length; i += 2) {
            byte[] temp = new byte[2];
            temp[0] = realData[i];
            temp[1] = realData[i + 1];
            finalDataArr[i / 2] = DigitalUtils.byte2Short(temp);
        }
        return finalDataArr;
    }
}

package com.detect.detect.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tjstudy.tcplib.RequestCallback;
import com.tjstudy.tcplib.ResponseCallback;
import com.tjstudy.tcplib.TCPClient;
import com.tjstudy.tcplib.utils.DigitalUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//    @BindView(R.id.cancel_bt)
//    Button cancelBt;
    private String mProjectName;
    private int mBuildSerialNum;
    private static final String IP = "10.10.100.254";
    private static final int PORT = 8899;
    private TCPClient tcpClient;
    private byte[] heartCommand, confirmCommand, startCommand;
    private TestPoint mTestPoint;
    private LineChart mChart;
    private LiuHandler handler;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private Timer timer;
    private LineData data;

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
//        initHandler();
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                int[] ints = new int[10];
//                for (int i = 0; i < ints.length; i++) {
//                    ints[i] = (int) (Math.random() * 2000);
//                }
//                waveDataList.add(ints);
//                if (waveDataList.size() >= 3) {
//                    timer.cancel();
//                }
//                setData1();
//            }
//        }, 1000);
        commonTitleTv.setText("检测");
        ToastUtils.showToast("将开始三次撞击测试");
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(SkipActivityConstant.DETECT_TEST_POINT_TEST_POINT)) {
                mTestPoint = (TestPoint) intent.getSerializableExtra(SkipActivityConstant.DETECT_TEST_POINT_TEST_POINT);
                buildSerialNumEt.setText(mTestPoint.getBuildSerialNum());
            }
        }
        initChart();
//        handler.sendEmptyMessage(0);
    }

//    private void initHandler() {
//        handler = new LiuHandler(this, new IHandleMessage() {
//            @Override
//            public void handleMessage(Message msg) {
//                service.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        int[] ints = new int[1000];
//                        for (int i = 0; i < ints.length; i++) {
//                            ints[i] = (int) (Math.random() * 2000);
//                        }
//                        waveDataList.add(ints);
//                        setData1();
//                        if (waveDataList.size() < 3) {
//                            handler.sendEmptyMessageDelayed(0, 3000);
//                        }
////                    DetectActivity.this.runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////
////                        }
////                    });
//                    }
//                });
//
//            }
//        });
//    }

    private void initChart() {
        mChart = findViewById(R.id.line_char);
        mChart.setViewPortOffsets(0, 0, 0, 0);
        mChart.setBackgroundColor(Color.parseColor("#A9A9A9"));

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setMaxHighlightDistance(300);
//        XAxis x = mChart.getXAxis();
//        x.setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setLabelCount(40);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.WHITE);

        YAxis y = mChart.getAxisLeft();
//        y.setTypeface(mTfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);
        mChart.setBackgroundColor(Color.WHITE);
//        mChart.getXAxis().setDrawLabels(false);
        mChart.getXAxis().setXOffset(50);
        mChart.getXAxis().setLabelCount(12);
        mChart.getXAxis().setTextColor(Color.BLUE);

        mChart.getAxisRight().setEnabled(false);

        // add data
//        setData(1000, 4096);
//        setData1();
        setDataDefalut();
        mChart.getLegend().setEnabled(false);

        mChart.animateXY(2000, 2000);
        mChart.setVisibleXRange(0.0f, 1000f);


//        for (IDataSet set : mChart.getData().getDataSets())
//            set.setDrawValues(!set.isDrawValuesEnabled());
////        mChart.invalidate();
//        // dont forget to refresh the drawing
//        mChart.invalidate();
    }

    private void setData1() {
        int[] ints1 = new int[1000];
        int[] ints2 = new int[1000];
        int[] ints3 = new int[1000];
        for (int i = 0; i < ints1.length; i++) {
            ints1[i] = (int) (Math.random() * 2000);
        }
        for (int i = 0; i < ints2.length; i++) {
            ints2[i] = (int) (Math.random() * 2000);
        }

        for (int i = 0; i < ints3.length; i++) {
            ints3[i] = (int) (Math.random() * 2000);
        }
        waveDataList.add(ints1);
        waveDataList.add(ints2);
        waveDataList.add(ints3);
        if (waveDataList.size() == 0) {
            return;
        }
        if (waveDataList.size() == 1) {
            ArrayList<Entry> yVals1 = new ArrayList<>();
            int[] intArr1 = waveDataList.get(0);
            for (int i = 0; i < intArr1.length; i++) {
                yVals1.add(new Entry(i, intArr1[i]));
            }
            LineDataSet set1;
//            if (mChart.getData() != null &&
//                    mChart.getData().getDataSetCount() >= 1) {
//                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
//                set1.setValues(yVals1);
//                mChart.getData().notifyDataChanged();
//                mChart.notifyDataSetChanged();
//            } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");
            getSetList(set1);
//            DataSet
            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            // set data
            mChart.setData(data);
//            }
        } else if (waveDataList.size() == 2) {
            ArrayList<Entry> yVals1 = new ArrayList<>();
            ArrayList<Entry> yVals2 = new ArrayList<>();
            int[] intArr1 = waveDataList.get(0);
            int[] intArr2 = waveDataList.get(1);
            for (int i = 0; i < intArr1.length; i++) {
                yVals1.add(new Entry(i, intArr1[i]));
            }

            for (int i = 0; i < intArr2.length; i++) {
                yVals2.add(new Entry(i, intArr2[i]));
            }
            LineDataSet set1, set2;
//            if (mChart.getData() != null &&
//                    mChart.getData().getDataSetCount() >= 2) {
//                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
//                set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
//                set1.setValues(yVals1);
//                set2.setValues(yVals2);
//                mChart.getData().notifyDataChanged();
//                mChart.notifyDataSetChanged();
//            } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");
            set2 = new LineDataSet(yVals2, "DataSet 2");
            getSetList(set1);
            getSetList(set2);
            // create a data object with the datasets
            LineData data = new LineData(set1, set2);
//            data.addDataSet(set1);
//            data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            // set data
            mChart.setData(data);
//            }
        } else if (waveDataList.size() == 3) {
            ArrayList<Entry> yVals1 = new ArrayList<>();
            ArrayList<Entry> yVals2 = new ArrayList<>();
            ArrayList<Entry> yVals3 = new ArrayList<>();
            int[] intArr1 = waveDataList.get(0);
            int[] intArr2 = waveDataList.get(1);
            int[] intArr3 = waveDataList.get(2);
            for (int i = 0; i < intArr1.length; i++) {
                yVals1.add(new Entry(i, intArr1[i]));
            }

            for (int i = 0; i < intArr2.length; i++) {
                yVals2.add(new Entry(i, intArr2[i]));
            }
            for (int i = 0; i < intArr3.length; i++) {
                yVals3.add(new Entry(i, intArr3[i]));
            }
            LineDataSet set1, set2, set3;
//            if (mChart.getData() != null &&
//                    mChart.getData().getDataSetCount() >= 3) {
//                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
//                set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
//                set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
//                set1.setValues(yVals1);
//                set2.setValues(yVals2);
//                set3.setValues(yVals3);
//                mChart.getData().notifyDataChanged();
//                mChart.notifyDataSetChanged();
//            } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(null, "DataSet 1");
            set2 = new LineDataSet(null, "DataSet 2");
            set3 = new LineDataSet(null, "DataSet 3");
            getSetList(set1);
            getSetList(set2);
            getSetList(set3);
            // create a data object with the datasets
            LineData data = new LineData(set1, set2, set3);
//            data.addDataSet(set1);
//            data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            // set data
            mChart.setData(data);
//            }
        }

        for (IDataSet set : mChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());
//        mChart.invalidate();
        // dont forget to refresh the drawing
        mChart.postInvalidate();
    }

    public void setDataDefalut() {

        LineDataSet set1, set2, set3;
        set1 = new LineDataSet(null, "DataSet 1");
        set2 = new LineDataSet(null, "DataSet 2");
        set3 = new LineDataSet(null, "DataSet 3");
//            data.addDataSet(set1);
//            data.setValueTypeface(mTfLight);
        data =  new LineData(set1, set2, set3);
        data.setValueTextSize(9f);
        data.setDrawValues(false);
        mChart.setData(data);

//        LineDataSet set1, set2, set3;
//        // create a dataset and give it a type
//        set1 = new LineDataSet(null, "DataSet 1");
//        set2 = new LineDataSet(null, "DataSet 2");
//        set3 = new LineDataSet(null, "DataSet 3");
//        getSetList(set1);
//        getSetList(set2);
//        getSetList(set3);
//        // create a data object with the datasets
//        LineData data = new LineData(set1, set2, set3);
////            data.addDataSet(set1);
////            data.setValueTypeface(mTfLight);
//        data.setValueTextSize(9f);
//        data.setDrawValues(false);
//        // set data
//        mChart.setData(data);
//        for (IDataSet set : mChart.getData().getDataSets())
//            set.setDrawValues(!set.isDrawValuesEnabled());
//        // dont forget to refresh the drawing
//        mChart.invalidate();
    }

    public void addData() {
//        int[] ints1 = new int[10];
//        for (int i = 0; i < ints1.length; i++) {
//            ints1[i] = (int) (Math.random() * 2000);
//        }
////        waveDataList.add(ints1);
        ArrayList<Entry> yVals1 = new ArrayList<>();
//        int[] intArr1 = waveDataList.get(0);
        for (int i = 0; i < 1000; i++) {
            yVals1.add(new Entry(i, (int) (Math.random() * 2000)));
        }
        LineData data1 = mChart.getData();
//        LineDataSet set1;
//        // create a dataset and give it a type
//        set1 = new LineDataSet(yVals1, "DataSet 1");
//        data1.addDataSet(set1);
//
        int dataSetCount = data1.getDataSetCount();
        for (int i = 0; i < dataSetCount; i++) {
            LineDataSet dataSetByIndex = (LineDataSet) data1.getDataSetByIndex(i);
            dataSetByIndex.setValues(yVals1);
        }
        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();


//        LineDataSet set1;
//        // create a dataset and give it a type
//        set1 = new LineDataSet(yVals1, "DataSet 1");
//        getSetList(set1);
////            DataSet
//        // create a data object with the datasets
//        LineData data1 = mChart.getData();
//        if (data1 == null) {
//            data1 = new LineData();
//        }
//        data1.addDataSet(set1);
////        LineData data = new LineData(set1);
//        data1.setValueTextSize(9f);
//        data1.setDrawValues(false);
//        // set data
//        mChart.setData(data1);
    }

    private void getSetList(LineDataSet set) {
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        //set1.setDrawFilled(true);
        set.setDrawCircles(false);
        set.setLineWidth(1.8f);
        set.setCircleRadius(4f);
        set.setCircleColor(Color.WHITE);
        set.setHighLightColor(Color.WHITE);
        set.setColor(Color.RED);
        set.setFillColor(Color.WHITE);
        set.setFillAlpha(100);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });
    }

    @OnClick({R.id.common_back_ll,/* R.id.cancel_bt,*/ R.id.detect_state_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll:
                finish();
                break;
//            case R.id.cancel_bt:
//                startActivity(new Intent(this, DetectFinishedActivity.class));
//                break;
            case R.id.detect_state_tv:
//                sendCommand(confirmCommand);
                addData();
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
                    ArrayList<com.detect.detect.db.WaveData> waveDataArr = new ArrayList<>();

                    for (int[] ints : waveDataList) {
                        com.detect.detect.db.WaveData waveData = new com.detect.detect.db.WaveData();
                        waveData.setWaveArr(ints);
                        waveDataArr.add(waveData);
                    }
                    String waveListStr = JSON.toJSONString(waveDataArr);

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
                        //成功跳转后销毁本界面
                        finish();
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
                    detectStateTv.setText("测试点 " + m + " 数据接收成功");
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

package com.detect.detect.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.detect.detect.BuildConfig;
import com.detect.detect.R;
import com.detect.detect.constant.GlobalContants;
import com.detect.detect.constant.SkipActivityConstant;
import com.detect.detect.global.GlobalApplication;
import com.detect.detect.shared_preferences.ProjectDataManager;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.socket.HexUtils;
import com.detect.detect.utils.AppUtils;
import com.detect.detect.utils.DisplayUtils;
import com.detect.detect.utils.PrefUtils;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.utils.UIUtils;
import com.detect.detect.widgets.DrawView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.printer.sdk.CanvasPrint;
import com.printer.sdk.FontProperty;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;
import com.tjstudy.tcplib.RequestCallback;
import com.tjstudy.tcplib.ResponseCallback;
import com.tjstudy.tcplib.TCPClient;
import com.tjstudy.tcplib.utils.DigitalUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yu on 18/1/13.
 */

public class DetectActivity extends BaseActivity {
    //todo 测试模式
    private boolean isCanPrint = true;
    private final static int SCANNIN_GREQUEST_CODE = 2;
    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 3;
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

    @BindView(R.id.ddd)
    ImageView ddddImage;
    //    @BindView(R.id.rr_2)
//    LinearLayout rr2;
    @BindView(R.id.s2_et)
    EditText detectDataS2Et;
    //    @BindView(R.id.rr_3)
//    LinearLayout rr3;
    @BindView(R.id.s3_et)
    EditText detectDataS3Et;
    @BindView(R.id.draw_view)
    DrawView drawView;

    @BindView(R.id.print_data_bt)
    Button print_data_bt;
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
    private BluetoothAdapter mBtAdapter;
    private String devicesAddress;
    private String devicesName;
    private BluetoothDevice mDevice;
    private PrinterInstance myPrinter;
    private boolean isConnected;
    private ProgressDialog dialog;

    //    private LineChart mChart;
    @Override
    protected void initData() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
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
        // 初始化对话框
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("正在连接");
        dialog.setMessage("请稍等");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        ToastUtils.showToast("将开始三次撞击测试");
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(SkipActivityConstant.DETECT_TEST_POINT_TEST_POINT)) {
                mTestPoint = (TestPoint) intent.getSerializableExtra(SkipActivityConstant.DETECT_TEST_POINT_TEST_POINT);
                buildSerialNumEt.setText(mTestPoint.getBuildSerialNum());
            }
        }
//        mChart = findViewById(R.id.line_char);
//        initChart();
//        handler.sendEmptyMessage(0);
    }


    @OnClick({R.id.common_back_ll, R.id.detect_state_tv, R.id.print_data_bt})
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
//                initChar1(ints);
                break;
            case R.id.print_data_bt:
                if (!isCanPrint) {
                    ToastUtils.showToast("测试尚未完成,请稍后打印");
                    return;
                }
                printData();
                break;

        }
    }

    private void printData() {
        if (!(mBtAdapter == null)) {
            if (isConnected) {
                printTheData(myPrinter);
                return;
            }
            // 判断设备蓝牙功能是否打开
            if (!mBtAdapter.isEnabled()) {
                // 打开蓝牙功能
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, ENABLE_BT);
            } else {
                // mDevice
                devicesAddress = PrefUtils.getString(this, GlobalContants.DEVICEADDRESS, "");
                if (devicesAddress == null || devicesAddress.length() <= 0) {
                    Intent intent = new Intent(DetectActivity.this, SearchDeviceActivity.class);
                    startActivityForResult(intent, CONNECT_DEVICE);
//                    Toast.makeText(SettingActivity.this, "您是第一次启动程序，请选择重新搜索连接！",
//                            Toast.LENGTH_SHORT).show();
                } else {
                    connect2BlueToothdevice();
                }
            }
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
                    isCanPrint = true;
                    //全部测试完成
                    Log.d(TAG, "onRec: 全部波形数据接收完成");
//                    Intent intent = new Intent(DetectActivity.this, DetectFinishedActivity.class);
//                    intent.putExtra("WAVE_DATA", waveDataList);
//                    startActivity(intent);
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
//                        finish();
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
                    //set
//                    initChar1(finalDataArr);
                    drawView.setData(finalDataArr);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECT_DEVICE) {// 连接设备
            devicesAddress = data.getExtras().getString(SearchDeviceActivity.EXTRA_DEVICE_ADDRESS);
            devicesName = data.getExtras().getString(SearchDeviceActivity.EXTRA_DEVICE_NAME);
            connect2BlueToothdevice();
        }
    }

    private void connect2BlueToothdevice() {
        // dialog.show();
        mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(devicesAddress);
        devicesName = mDevice.getName();
        myPrinter = PrinterInstance.getPrinterInstance(mDevice, mHandler);
        if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {// 未绑定
            // IntentFilter boundFilter = new IntentFilter();
            // boundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            // mContext.registerReceiver(boundDeviceReceiver, boundFilter);
            PairOrConnect(true);
        } else {
            PairOrConnect(false);
        }
    }

    private BroadcastReceiver boundDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!mDevice.equals(device)) {
                    return;
                }
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.i(TAG, "bounding......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.i(TAG, "bound success");
                        // if bound success, auto init BluetoothPrinter. open
                        // connect.
                        unregisterReceiver(boundDeviceReceiver);
                        dialog.show();
                        // 配对完成开始连接
                        if (myPrinter != null) {
                            new connectThread().start();
                        }
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.i(TAG, "执行顺序----4");
                        unregisterReceiver(boundDeviceReceiver);
                        Log.i(TAG, "bound cancel");
                        break;
                    default:
                        break;
                }

            }
        }
    };

    private void PairOrConnect(boolean pair) {
        if (pair) {
            IntentFilter boundFilter = new IntentFilter();
            boundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            registerReceiver(boundDeviceReceiver, boundFilter);
            boolean success = false;
            try {
                // // 自动设置pin值
                // Method autoBondMethod =
                // BluetoothDevice.class.getMethod("setPin", new Class[] {
                // byte[].class });
                // boolean result = (Boolean) autoBondMethod.invoke(mDevice, new
                // Object[] { "1234".getBytes() });
                // Log.i(TAG, "setPin is success? : " + result);

                // 开始配对 这段代码打开输入配对密码的对话框
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                success = (Boolean) createBondMethod.invoke(mDevice);
                // // 取消用户输入
                // Method cancelInputMethod =
                // BluetoothDevice.class.getMethod("cancelPairingUserInput");
                // boolean cancleResult = (Boolean)
                // cancelInputMethod.invoke(mDevice);
                // Log.i(TAG, "cancle is success? : " + cancleResult);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "createBond is success? : " + success);
        } else {
            new connectThread().start();

        }
    }

    private class connectThread extends Thread {
        @Override
        public void run() {
            if (myPrinter != null) {
                isConnected = myPrinter.openConnection();
            }
        }
    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {

                if (device != null && myPrinter != null && isConnected && device.equals(mDevice)) {
                    myPrinter.closeConnection();
                    mHandler.obtainMessage(PrinterConstants.Connect.CLOSED).sendToTarget();
                }
            }

        }
    };

    /**
     * 设置TSPL指令打印机
     *
     * @param mPrinter
     * @return 是否设置成功
     */
    private boolean setPrinterTSPL(final PrinterInstance mPrinter) {
        boolean isSettingSuccess = false;
        String gapWidth = "";
        String gapOffset = "";
        String printSpeed = "";
        String printLevel = "";
        String printLabelOffset = "";
        String printNumbers = "";
        String printLeftMargin = "";
        String printTopMargin = "";
        for (int i = 0; i < 1; i++) {
            try {
                // 处于TSPL指令模式下
//                if (llTSPL.getVisibility() == View.VISIBLE) {
                // 设置标签间的缝隙大小
                if (gapWidth == null || gapWidth.equals("") || gapOffset == null || gapOffset.equals("")) {
//                    Toast.makeText(GlobalApplication.getContext(), "间隙宽度和间隙偏移量不能为空",  Toast.LENGTH_SHORT).show();
//                    break;
                } else {
                    int gapWidthTSPL = Integer.parseInt(gapWidth);
                    int gapOffsetTSPL = Integer.parseInt(gapOffset);
                    mPrinter.setGAPTSPL(gapWidthTSPL, gapOffsetTSPL);
                    PrefUtils.setInt(GlobalApplication.getContext(), "gapwidthtspl", gapWidthTSPL);
                    PrefUtils.setInt(GlobalApplication.getContext(), "gapoffsettspl", gapOffsetTSPL);
                }
                // 设置打印机打印速度
                if (printSpeed == null || printSpeed.equals("")) {
//                    Toast.makeText(GlobalApplication.getContext(), "打印机打印速度不能设置为空",  Toast.LENGTH_SHORT).show();
//                    break;
                } else {
                    int printSpeedTSPL = Integer.parseInt(printSpeed);
                    mPrinter.setPrinterTSPL(PrinterConstants.CommandTSPL.SPEED, printSpeedTSPL);
                    PrefUtils.setInt(GlobalApplication.getContext(), "printspeed", printSpeedTSPL);
                }
                // 设置打印机打印浓度
                if (printLevel == null || printLevel.equals("")) {
//                    Toast.makeText(GlobalApplication.getContext(), "打印机打印浓度不能设置为空", Toast.LENGTH_SHORT).show();
//                    break;
                } else {
                    int printLevelSpeedTSPL = Integer.parseInt(printLevel);
                    mPrinter.setPrinterTSPL(PrinterConstants.CommandTSPL.DENSITY, printLevelSpeedTSPL);
                    PrefUtils.setInt(GlobalApplication.getContext(), "printlevel", printLevelSpeedTSPL);
                }
                // 设置标签偏移量
                if (printLabelOffset == null || printLabelOffset.equals("")) {
//                    Toast.makeText(GlobalApplication.getContext(), "标签偏移量不能设置为空", Toast.LENGTH_SHORT).show();
//                    break;
                } else {
                    // 标签偏移量（点数）
                    int labelOffsetTSPL = Integer.parseInt(printLabelOffset) * 8;
                    mPrinter.setPrinterTSPL(PrinterConstants.CommandTSPL.SHIFT, labelOffsetTSPL);
                    PrefUtils.setInt(GlobalApplication.getContext(), "labeloffsettspl", labelOffsetTSPL / 8);

                }
                // 设置打印机打印份数
                if (printNumbers == null || printNumbers.equals("")) {
//                    Toast.makeText(GlobalApplication.getContext(), "打印机打印数量不能设置为空或者为负数", Toast.LENGTH_SHORT).show();
//                    break;
                } else {
                    int printNumbersTSPL = Integer.parseInt(printNumbers);
                    PrefUtils.setInt(GlobalApplication.getContext(), "printnumbers", printNumbersTSPL);
                }

                // 设置打印内容初始位置
                if (printLeftMargin == null || printLeftMargin.equals("") || printTopMargin == null
                        || printLeftMargin.equals("")) {
//                    Toast.makeText(GlobalApplication.getContext(), "标签偏移量不能为空或者为负数", Toast.LENGTH_SHORT).show();
//                    break;
                } else {
                    int marginLeft = Integer.parseInt(printLeftMargin);
                    PrefUtils.setInt(GlobalApplication.getContext(), "leftmargin", marginLeft);
                    int marginTop = Integer.parseInt(printTopMargin);
                    PrefUtils.setInt(GlobalApplication.getContext(), "topmargin", marginTop);

                }

                // // 撕纸设置
                // if (tearSet == null || tearSet.equals("")) {
                // break;
                // } else if (tearSet.equals("撕纸")) {
                // mPrinter.setPrinterTSPL(CommandTSPL.TEAR, 1);
                // mPrinter.setPrinterTSPL(CommandTSPL.PEEL, 0);
                // PrefUtils.setInt(mContext, "tearAndpeel", 0);
                // } else if (tearSet.equals("剥纸")) {
                // mPrinter.setPrinterTSPL(CommandTSPL.PEEL, 1);
                // PrefUtils.setInt(mContext, "tearAndpeel", 1);
                // } else if (tearSet.equals("关")) {
                // mPrinter.setPrinterTSPL(CommandTSPL.TEAR, 0);
                // mPrinter.setPrinterTSPL(CommandTSPL.PEEL, 0);
                // PrefUtils.setInt(mContext, "tearAndpeel", 2);
//                //
//                // }
//                // 开关钱箱设置
//                if (OpenCashSet == null || OpenCashSet.equals("")) {
//                    break;
//                } else if (OpenCashSet.equals(R.string.String_judge1)) {
//                    PrefUtils.setInt(mContext, "isOpenCash", 0);
//                } else if (OpenCashSet.equals(R.string.String_judge2)) {
//                    PrefUtils.setInt(mContext, "isOpenCash", 1);
//                } else if (OpenCashSet.equals(R.string.String_judge3)) {
//                    PrefUtils.setInt(mContext, "isOpenCash", 2);
//                }
//                // 开关蜂鸣器设置
//                if (IsBeepSet == null || IsBeepSet.equals("")) {
//                    break;
//                } else if (IsBeepSet.equals(R.string.String_judge4)) {
//                    PrefUtils.setInt(mContext, "isBeep", 2);
//                } else if (IsBeepSet.equals(R.string.String_judge5)) {
//                    PrefUtils.setInt(mContext, "isBeep", 1);
//                } else if (IsBeepSet.equals(R.string.String_judge6)) {
//                    PrefUtils.setInt(mContext, "isBeep", 0);
//
//                }

                isSettingSuccess = true;

//                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                Toast.makeText(GlobalApplication.getContext(), "向打印机写入数据异常", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (PrinterPortNullException e) {
                Toast.makeText(GlobalApplication.getContext(), "打印机为空异常", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (ParameterErrorException e) {
                Toast.makeText(GlobalApplication.getContext(), "传入参数异常", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(GlobalApplication.getContext(), "其他未知异常", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return isSettingSuccess;
    }

    private IntentFilter bluDisconnectFilter;
    // 用于接受连接状态消息的 Handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    ToastUtils.showToast("打印中,请稍等");
                    isConnected = true;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    PrefUtils.setString(DetectActivity.this, GlobalContants.DEVICEADDRESS, devicesAddress);
                    bluDisconnectFilter = new IntentFilter();
                    bluDisconnectFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                    registerReceiver(myReceiver, bluDisconnectFilter);
//                    hasRegDisconnectReceiver = true;
                    // TOTO 暂时将TSPL指令设置参数的设置放在这
                    if (setPrinterTSPL(myPrinter)) {
//                        Toast.makeText(DetectActivity.this, R.string.settingactivitty_toast_bluetooth_set_tspl_successful, Toast.LENGTH_SHORT)
//                                .show();
                    }
                    printTheData(myPrinter);
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    Toast.makeText(DetectActivity.this, "连接失败,稍后重试", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "连接失败,稍后重试");
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    Toast.makeText(DetectActivity.this, "连接关闭,稍后重试", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "连接关闭,稍后重试");
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    Toast.makeText(DetectActivity.this, R.string.conn_no, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(DetectActivity.this, "打印机通信正常!", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(DetectActivity.this, "打印机通信异常常，请检查蓝牙连接!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                case -2:
                    Toast.makeText(DetectActivity.this, "打印机缺纸!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                case -3:
                    Toast.makeText(DetectActivity.this, "打印机开盖!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                // case 10:
                // if (setPrinterTSPL(myPrinter)) {
                // Toast.makeText(GlobalApplication.getContext(), "蓝牙连接设置TSPL指令成功", 0).show();
                // }
                default:
                    break;
            }

            updateButtonState(isConnected);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    };

    private void printTheData(final PrinterInstance myPrinter) {
        final String picPath = drawView.viewSaveToImage();
        String space = "\r\n" + "\r\n";
//        String subsidence = mTestPoint.getSubsidence();
        String str1 = detectDataS1Et.getText().toString().trim();
        String str2 = detectDataS2Et.getText().toString().trim();
        String str3 = detectDataS3Et.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String text = space + "仪器编号" + space + mTestPoint.getInstrumentNumber()
                + space + "检测人" + space + mTestPoint.getDetectPerson() + space
                + "工程名称/测试点" + space + mTestPoint.getProjectName()
                + space + "施工单位" + space
                + mTestPoint.getConstructionOrganization() + space
                + "填料类型" + space + mTestPoint.getFillerType() + space +
                "测试时间" + space + format.format(mTestPoint.getDetectTime()) + space
                + "测试序号" + space + mTestPoint.getBuildSerialNum() + space +
                "S1=" + str1 + space +
                "S2=" + str2 + space +
                "S3=" + str3 + space;
        new Thread(new Runnable() {
            public void run() {
                myPrinter.printText(text + "\r\n");
                Bitmap bitmap = null;
                if (!TextUtils.isEmpty(picPath)) {
                    bitmap = resizeBitmap(BitmapFactory.decodeFile(picPath));
                    //创建画布
                    CanvasPrint cp = new CanvasPrint();
                    //初始化画布，POS88系列用T9参数即可
                    cp.init(PrinterConstants.PrinterType.T9);
                    //将二维码画到画布上（0,0）处坐标
                    cp.drawImage(0, 0, bitmap);
                    //将画布保存成图片并进行打印
                    myPrinter.printImage(cp.getCanvasImage(), PrinterConstants.PAlign.NONE, 0, false);
                    String space1 = "\r\n" + "\r\n";
                    myPrinter.printText(space1);
                }
            }
        }).start();
    }

    /**
     * 使用Matrix将Bitmap压缩到指定大小
     *
     * @param bitmap
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(0.35f, 0.35f);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    private void updateButtonState(boolean isConnected) {
        if (isConnected) {
        } else {

        }
        PrefUtils.setBoolean(DetectActivity.this, GlobalContants.CONNECTSTATE, isConnected);
    }
}

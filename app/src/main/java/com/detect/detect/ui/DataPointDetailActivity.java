package com.detect.detect.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.detect.detect.R;
import com.detect.detect.constant.GlobalContants;
import com.detect.detect.db.FileConstant;
import com.detect.detect.global.GlobalApplication;
import com.detect.detect.shared_preferences.Project;
import com.detect.detect.shared_preferences.ProjectDataManager;
import com.detect.detect.shared_preferences.TestPoint;
import com.detect.detect.utils.CSVUtils;
import com.detect.detect.utils.PrefUtils;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.widgets.DrawView;
import com.gsh.dialoglibrary.RaiingAlertDialog;
import com.printer.sdk.CanvasPrint;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DataPointDetailActivity extends BaseActivity {
    @BindView(R.id.data_draw_view)
    DrawView dataDrawView;
    @BindView(R.id.content_tv)
    TextView contentTv;

    @BindView(R.id.common_title2_tv)
    TextView common_title2_tv;
    @BindView(R.id.save_tv)
    TextView save_tv;
    @BindView(R.id.print_tv)
    TextView print_tv;

    @BindView(R.id.common_back_ll2)
    View common_back_ll2;
    private BluetoothAdapter mBtAdapter;
    private String devicesAddress;
    private String devicesName;
    private BluetoothDevice mDevice;
    private PrinterInstance myPrinter;
    private boolean isConnected;
    private ProgressDialog dialog;
    private static final String TAG = "DataPointDetailActivity";
    TestPoint mTestPoint;
    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 3;

    @OnClick({R.id.common_back_ll2, R.id.save_tv, R.id.print_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_back_ll2:
                finish();
                break;
            case R.id.save_tv:
                new RaiingAlertDialog(this, "提示"
                        , "确定将文件导出到sdcard?", "确定", "取消", new RaiingAlertDialog.CallbackRaiingAlertDialog() {
                    @Override
                    public void onPositive() {
                        outputData(mTestPoint.getProjectName());
                    }

                    @Override
                    public void onNegative() {

                    }
                }).show();
                break;
            case R.id.print_tv:
                printData();
                break;
        }
    }

    private void outputData(String projectName) {
        if (TextUtils.isEmpty(projectName)) {
            ToastUtils.showToast("请选择工程或者工程节点");
            return;
        }
        File file = new File(FileConstant.OUT_PUT_PATH + projectName + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = FileConstant.OUT_PUT_PATH + projectName + File.separator + projectName + ".csv";
        File fileDir = new File(path);
        if (fileDir.exists()) {
            fileDir.delete();
        }
        try {
            boolean newFile = fileDir.createNewFile();
            Log.d(TAG, "outputData: newFile: " + newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Project> allProjects = ProjectDataManager.getInstance().getAllProjects();
        if (allProjects != null && allProjects.size() > 0) {
            for (Project project : allProjects) {
                if (project == null) {
                    continue;
                }
                if (TextUtils.equals(project.getProjectName(), projectName)) {
                    List<TestPoint> testPointList = project.getTestPointList();
                    if (testPointList != null && testPointList.size() > 0) {
                        CSVUtils.writeTestPointListToCSV(fileDir.getAbsolutePath(), testPointList);
                    }
                    //导出图片到指定文件夹
                    saveTestPointsPic(projectName, testPointList);
                }
            }
        }
    }

    private void saveTestPointsPic(String projectName, List<TestPoint> testPointList) {
        for (TestPoint testPoint : testPointList) {
            if (testPoint == null) {
                continue;
            }
            String picPath = testPoint.getPicPath();
            if (!TextUtils.isEmpty(picPath)) {
                savePicToLocal(projectName, picPath, testPoint);
            }
        }
    }

    private void savePicToLocal(String projectName, String oldPicPath, TestPoint testPoint) {
        String newPath = FileConstant.OUT_PUT_PATH + projectName;
        File fileDir = new File(newPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File newFile = new File(newPath, testPoint.getBuildSerialNum() + ".jpg");
        if (newFile.exists()) {
            newFile.delete();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        copyPic(oldPicPath, newFile.getAbsolutePath());

    }

    private void copyPic(String oldPicPath, String newPicPath) {
        int length;
        try {
            FileInputStream fis = new FileInputStream(oldPicPath);
            FileOutputStream fos = new FileOutputStream(newPicPath);
            byte[] buffer = new byte[1024 * 8];
            while ((length = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        common_title2_tv.setText("数据管理");
        Intent intent = getIntent();
        if (intent != null) {
            mTestPoint = (TestPoint) intent.getSerializableExtra(GlobalContants.SKIP_DATA_POINT_BUNDLE);
        }
        if (mTestPoint == null) {
            throw new IllegalArgumentException("testPoint不可能为null,检测代码");
        }
        String subsidence = mTestPoint.getSubsidence();
        String str1 = "";
        String str2 = "";
        String str3 = "";
        if (!TextUtils.isEmpty(subsidence)) {
            String[] split = subsidence.split("_");
            str1 = split[0];
            str2 = split[1];
            str3 = split[2];
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String space = "\r\n" + "\r\n";
        final String text = "仪器编号" + space + mTestPoint.getInstrumentNumber()
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
        contentTv.setText(text);
    }

    private void printTheData(final PrinterInstance myPrinter) {
        final String picPath = dataDrawView.viewSaveToImage();
        String space = "\r\n" + "\r\n";
//        String subsidence = mTestPoint.getSubsidence();
        String subsidence = mTestPoint.getSubsidence();
        String str1 = "";
        String str2 = "";
        String str3 = "";
        if (!TextUtils.isEmpty(subsidence)) {
            String[] split = subsidence.split("_");
            str1 = split[0];
            str2 = split[1];
            str3 = split[2];
        }
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
        PrefUtils.setBoolean(DataPointDetailActivity.this, GlobalContants.CONNECTSTATE, isConnected);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_detail;
    }

    @Override
    public void initView() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

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
                            new DataPointDetailActivity.connectThread().start();
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
            new DataPointDetailActivity.connectThread().start();

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
                    PrefUtils.setString(DataPointDetailActivity.this, GlobalContants.DEVICEADDRESS, devicesAddress);
                    bluDisconnectFilter = new IntentFilter();
                    bluDisconnectFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                    registerReceiver(myReceiver, bluDisconnectFilter);
//                    hasRegDisconnectReceiver = true;
                    // TOTO 暂时将TSPL指令设置参数的设置放在这
                    if (setPrinterTSPL(myPrinter)) {
//                        Toast.makeText(DataPointDetailActivity.this, R.string.settingactivitty_toast_bluetooth_set_tspl_successful, Toast.LENGTH_SHORT)
//                                .show();
                    }
                    printTheData(myPrinter);
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    Toast.makeText(DataPointDetailActivity.this, "连接失败,稍后重试", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "连接失败,稍后重试");
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    Toast.makeText(DataPointDetailActivity.this, "连接关闭,稍后重试", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "连接关闭,稍后重试");
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    Toast.makeText(DataPointDetailActivity.this, R.string.conn_no, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(DataPointDetailActivity.this, "打印机通信正常!", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(DataPointDetailActivity.this, "打印机通信异常常，请检查蓝牙连接!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                case -2:
                    Toast.makeText(DataPointDetailActivity.this, "打印机缺纸!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                case -3:
                    Toast.makeText(DataPointDetailActivity.this, "打印机开盖!", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(DataPointDetailActivity.this, SearchDeviceActivity.class);
                    startActivityForResult(intent, CONNECT_DEVICE);
//                    Toast.makeText(SettingActivity.this, "您是第一次启动程序，请选择重新搜索连接！",
//                            Toast.LENGTH_SHORT).show();
                } else {
                    connect2BlueToothdevice();
                }
            }
        }
    }
}

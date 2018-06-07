package com.detect.detect.ui;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.detect.detect.R;
import com.detect.detect.constant.GlobalContants;
import com.detect.detect.global.GlobalApplication;
import com.detect.detect.shared_preferences.CommonSP;
import com.detect.detect.utils.PrefUtils;
import com.detect.detect.utils.ToastUtils;
import com.detect.detect.utils.UIUtils;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yu on 18/1/13.
 */

public class SystemSettingsActivity extends BaseActivity {
    @BindView(R.id.common_back_ll)
    LinearLayout commonBackLl;
    @BindView(R.id.common_title_tv)
    TextView commonTitleTv;
    @BindView(R.id.settings_wifi_rl)
    RelativeLayout settings_wifi_rl;

    @BindView(R.id.settings_blue_rl)
    RelativeLayout settings_blue_rl;

    @BindView(R.id.machine_num_rl)
    RelativeLayout machine_num_rl;
    @BindView(R.id.line_correct_rl)
    RelativeLayout line_correct_rl;
    @BindView(R.id.language_set_rl)
    RelativeLayout language_set_rl;
    public static final int CONNECT_DEVICE = 1;
    private String devicesAddress;
    private String devicesName;
    private BluetoothDevice mDevice;
    private PrinterInstance myPrinter;
    private boolean isConnected;
    private static final String TAG = "SystemSettingsActivity";

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_system_settings;
    }

    @Override
    public void initView() {
        commonTitleTv.setText("设置");
    }


    @OnClick({R.id.common_back_ll, R.id.settings_wifi_rl
            , R.id.machine_num_rl, R.id.line_correct_rl, R.id.language_set_rl, R.id.settings_blue_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.settings_wifi_rl:
                break;
            case R.id.settings_blue_rl:
                Intent intent = new Intent(this, SearchDeviceActivity.class);
                startActivityForResult(intent, CONNECT_DEVICE);
                break;
            case R.id.machine_num_rl:
                break;
            case R.id.line_correct_rl:
                String password = CommonSP.getInstance().getPassword();
                if (TextUtils.isEmpty(password)) {
                    UIUtils.intentActivity(SetPasswordActivity.class, null, this);
                } else {
                    UIUtils.intentActivity(InputPasswordActivity.class, null, this);
                }
                break;
            case R.id.language_set_rl:
                break;
            case R.id.common_back_ll:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECT_DEVICE) {
            // 连接设备
            if (data == null) {
                return;
            }
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
            new SystemSettingsActivity.connectThread().start();

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
//                        dialog.show();
                        // 配对完成开始连接
                        if (myPrinter != null) {
                            new SystemSettingsActivity.connectThread().start();
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
                    PrefUtils.setString(SystemSettingsActivity.this, GlobalContants.DEVICEADDRESS, devicesAddress);
                    bluDisconnectFilter = new IntentFilter();
                    bluDisconnectFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                    registerReceiver(myReceiver, bluDisconnectFilter);
//                    hasRegDisconnectReceiver = true;
                    // TOTO 暂时将TSPL指令设置参数的设置放在这
                    if (setPrinterTSPL(myPrinter)) {
//                        Toast.makeText(SystemSettingsActivity.this, R.string.settingactivitty_toast_bluetooth_set_tspl_successful, Toast.LENGTH_SHORT)
//                                .show();
                    }
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    Toast.makeText(SystemSettingsActivity.this, "连接失败,稍后重试", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "连接失败,稍后重试");
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    Toast.makeText(SystemSettingsActivity.this, "连接关闭,稍后重试", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "连接关闭,稍后重试");
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    Toast.makeText(SystemSettingsActivity.this, R.string.conn_no, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(SystemSettingsActivity.this, "打印机通信正常!", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(SystemSettingsActivity.this, "打印机通信异常常，请检查蓝牙连接!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                case -2:
                    Toast.makeText(SystemSettingsActivity.this, "打印机缺纸!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                case -3:
                    Toast.makeText(SystemSettingsActivity.this, "打印机开盖!", Toast.LENGTH_SHORT).show();
//                    vibrator();
                    break;
                // case 10:
                // if (setPrinterTSPL(myPrinter)) {
                // Toast.makeText(GlobalApplication.getContext(), "蓝牙连接设置TSPL指令成功", 0).show();
                // }
                default:
                    break;
            }
        }
    };
}

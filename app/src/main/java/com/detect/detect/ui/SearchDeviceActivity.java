package com.detect.detect.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.detect.detect.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchDeviceActivity extends BaseActivity {
    private static final String TAG = "SearchDeviceActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_RE_PAIR = "re_pair";
    public static String EXTRA_DEVICE_NAME = "device_name";
    @BindView(R.id.device_rc)
    RecyclerView deviceRc;

    @BindView(R.id.backBt)
    Button backBt;
    @BindView(R.id.search_again_bt)
    Button search_again_bt;
    private List<String> deviceNames = new ArrayList<>();
    private MyAdapter myAdapter;
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activty_search_device;
    }

    @Override
    public void initView() {

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceNames.add(device.getName() + " ( " + getResources().getText(R.string.has_paired)
                        + " )" + "\n" + device.getAddress());
            }
        }
        myAdapter = new MyAdapter(this, deviceNames);
        deviceRc.setLayoutManager(new LinearLayoutManager(this));
        deviceRc.setAdapter(myAdapter);
        myAdapter.setOnClick(new MyAdapter.ItemClick() {
            @Override
            public void onItemClick(String info) {
                String address = info.substring(info.length() - 17);
                String name = info.substring(0, info.length() - 17);
                returnToPreviousActivity(address, false, name);
            }
        });

    }

    private void returnToPreviousActivity(String address, boolean re_pair, String name) {
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
        intent.putExtra(EXTRA_RE_PAIR, re_pair);
        intent.putExtra(EXTRA_DEVICE_NAME, name);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @OnClick({R.id.backBt, R.id.search_again_bt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBt:
                finish();
                break;

            case R.id.search_again_bt:
                doDiscovery();
                break;
        }

    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
//        setProgressBarIndeterminateVisibility(true);
//        setTitle(R.string.scanning);

        // If we're already discovering, stop it
        // if (mBtAdapter.isDiscovering()) {
        // mBtAdapter.cancelDiscovery();
        // }

        deviceNames.clear();
        myAdapter.notifyDataSetChanged();
        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(SearchDeviceActivity.this, "开始扫描搜索蓝牙设备", Toast.LENGTH_SHORT).show();
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String itemName = device.getName() + " ( "
                        + getResources().getText(device.getBondState() == BluetoothDevice.BOND_BONDED
                        ? R.string.has_paired : R.string.not_paired)
                        + " )" + "\n" + device.getAddress();

//                mPairedDevicesArrayAdapter.remove(itemName);
//                mPairedDevicesArrayAdapter.add(itemName);
//                pairedListView.setEnabled(true);
                if (!deviceNames.contains(itemName)) {
                    deviceNames.add(itemName);
                    myAdapter.notifyDataSetChanged();
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                setProgressBarIndeterminateVisibility(false);
//                setTitle(R.string.select_device);
//                if (mPairedDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = getResources().getText(R.string.none_found).toString();
//                    mPairedDevicesArrayAdapter.add(noDevices);
//                    pairedListView.setEnabled(false);
//                }
//                scanButton.setEnabled(true);
                Toast.makeText(SearchDeviceActivity.this, "蓝牙扫描已结束", Toast.LENGTH_SHORT).show();

            }

        }
    };
}

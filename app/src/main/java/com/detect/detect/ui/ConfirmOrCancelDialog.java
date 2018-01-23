package com.detect.detect.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.constant.CommonConstant;
import com.detect.detect.shared_preferences.TestPoint;

/**
 * 固件更新需要点击确认的提示对话框
 * Created by dongdong.yu on 2017/9/15.
 */

public class ConfirmOrCancelDialog extends DialogFragment {
    private static final String TAG = "ConfirmOrCancelDialog";
    private TextView mConfirmBt;
    private TextView mCancelBt;
    private String mProjectName;
    private int mBuildSerialNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_confirm_cancel, container, false);
        Bundle arguments = getArguments();
        mProjectName = arguments.getString(CommonConstant.PROJECT_NAME);
        mBuildSerialNum = arguments.getInt(CommonConstant.BUILD_SERIAL_NUM);
        initView(view);
        initClick();
        return view;
    }

    private void initClick() {
        mConfirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null && activity instanceof ConfirmClickCallback) {
                    ConfirmClickCallback callback = (ConfirmClickCallback) activity;
                    callback.onConfirm(mProjectName,mBuildSerialNum);
                }
                //点击确认后,弹框消失
                dismiss();
            }
        });
        mCancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确认后,弹框消失
                dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setDimAmount(0.25f);
                window.setLayout((int) (dm.widthPixels * 0.4), (int) (dm.heightPixels * 0.4) /*ViewGroup.LayoutParams.WRAP_CONTENT*/);
            }
        }
    }


    private void initView(View view) {
        mConfirmBt = view.findViewById(R.id.confirm_bt);
        mCancelBt = view.findViewById(R.id.cancel_bt);
    }

    interface ConfirmClickCallback {
        void onConfirm(String projectName, int buildSerialNum);

//        void onCancel(String trim);
    }
}

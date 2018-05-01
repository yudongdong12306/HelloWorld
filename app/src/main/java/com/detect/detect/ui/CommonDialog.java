package com.detect.detect.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.constant.CommonConstant;

/**
 * 固件更新需要点击确认的提示对话框
 * Created by dongdong.yu on 2017/9/15.
 */

public class CommonDialog extends DialogFragment {
    private TextView mConfirmBt;
    private TextView mCancelBt, title_tv;
    private String title, leftText, rightText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_common, container, false);
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
                    callback.onConfirm();
                }
                //点击确认后,弹框消失
                dismiss();
            }
        });
        mCancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确认后,弹框消失
                Activity activity = getActivity();
                if (activity != null && activity instanceof ConfirmClickCallback) {
                    ConfirmClickCallback callback = (ConfirmClickCallback) activity;
                    callback.onCancel();
                }
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
                window.setLayout((int) (dm.widthPixels * 0.7), (int) (dm.heightPixels * 0.23) /*ViewGroup.LayoutParams.WRAP_CONTENT*/);
            }
        }
    }


    public void setContent(String title, String leftText, String rightText) {
        this.title = title;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    private void initView(View view) {
        mConfirmBt = view.findViewById(R.id.confirm_bt);
        mCancelBt = view.findViewById(R.id.cancel_bt);
        title_tv = view.findViewById(R.id.title_tv);
        if (!TextUtils.isEmpty(title)) {
            title_tv.setText(title);
        } else {
            throw new IllegalArgumentException("没有title");
        }
        if (!TextUtils.isEmpty(leftText)) {
            mConfirmBt.setText(leftText);
        } else {
            mConfirmBt.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(rightText)) {
            mCancelBt.setText(rightText);
        } else {
            mCancelBt.setVisibility(View.GONE);
        }
    }

    interface ConfirmClickCallback {
        void onConfirm();

        void onCancel();

    }
}

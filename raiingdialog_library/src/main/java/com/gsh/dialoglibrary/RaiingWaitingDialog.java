package com.gsh.dialoglibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by damon on 6/19/15.
 * 通用等待框
 */
public class RaiingWaitingDialog {

    private MyAlertDialog dialog;
    /**
     * 默认背景是不变暗的
     */
    private boolean mIsScreenDim;


    public RaiingWaitingDialog(Context context, String text) {

        dialog = new MyAlertDialog(context, text);
    }

    public void setScreenDim(boolean isScreenDim) {
        mIsScreenDim = isScreenDim;
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 取消对话框
     */
    public void cancel() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    private class MyAlertDialog extends AlertDialog {

        private String mText;
        private Context context;

        private ImageView dialog_waiting_daisy;

        protected MyAlertDialog(Context context, String text) {
            super(context);
            mText = text;
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_waiting);
            setCanceledOnTouchOutside(false);
            setCancelable(false);
            TextView textView = (TextView) findViewById(R.id.dialog_waiting_text);
            dialog_waiting_daisy = (ImageView) findViewById(R.id.dialog_waiting_daisy);
            textView.setVisibility(mText != null ? View.VISIBLE : View.GONE);
            if (mText != null) {
                textView.setText(mText);
            }

        }

        @Override
        public void show() {
            super.show();
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
//            int screenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            if (!mIsScreenDim) {
                params.dimAmount = 0f;
            }
            window.setAttributes(params);
            window.setGravity(Gravity.CENTER);
            RotateAnimation animation = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1000);
            animation.setInterpolator(new LinearInterpolator());
//            animation.setRepeatMode(Animation.RESTART);
            animation.setRepeatCount(Animation.INFINITE);
            dialog_waiting_daisy.setAnimation(animation);
            animation.start();
        }
    }
}

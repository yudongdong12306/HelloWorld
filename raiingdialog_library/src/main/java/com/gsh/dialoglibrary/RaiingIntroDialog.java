package com.gsh.dialoglibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/** 显示介绍性文本用的Dialog
 * Created by xiaoxin on 2017/2/16.
 */

public class RaiingIntroDialog {

    private MyIntroDialog dialog;

    public RaiingIntroDialog(Activity activity, String text) {
        dialog = new MyIntroDialog(activity, text);
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

    private class MyIntroDialog extends AlertDialog {

        private String mText;
        private Activity mActivity;

        protected MyIntroDialog(Activity activity, String text){
            super(activity);
            mText = text;
            mActivity = activity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_intro);
            setCanceledOnTouchOutside(false);
            setCancelable(false);

            // 设置文本
            TextView textView = (TextView) findViewById(R.id.content_text_view);
            textView.setVisibility(mText != null ? View.VISIBLE : View.GONE);
            if (mText != null) {
                textView.setText(mText);
            }

            // 设置点击手势
            ImageView closeIcon = (ImageView)findViewById(R.id.close_icon);
            closeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyIntroDialog.this.cancel();
                }
            });
        }

        @Override
        public void show() {
            super.show();

//            Window window = getWindow();
//            WindowManager.LayoutParams params = window.getAttributes();
////            int screenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;
//            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//            window.setAttributes(params);
//            window.setGravity(Gravity.CENTER);
//            RotateAnimation animation = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//            animation.setDuration(1000);
//            animation.setInterpolator(new LinearInterpolator());
////            animation.setRepeatMode(Animation.RESTART);
//            animation.setRepeatCount(Animation.INFINITE);
////            dialog_waiting_daisy.setAnimation(animation);
//            animation.start();
        }
    }
}

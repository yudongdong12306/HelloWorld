package com.gsh.dialoglibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by damon on 6/18/15.
 * 通用对话框
 */
public class RaiingAlertDialog {

    private MyAlertDialog dialog;

    /**
     * @param activity       activity (为null时,崩溃)
     * @param title          标题（可为空，但是标题和正文必须存在一个）（标题有加粗，且字体稍微大些）
     * @param message        正文（可为空，但是标题和正文必须存在一个）
     * @param positiveButton 取消按钮（确定或取消按钮必须存在一个）（取消有字体加粗效果）
     * @param negativeButton 确认按钮（确定或取消按钮必须存在一个）
     */
    public RaiingAlertDialog(Activity activity, String title, String message, String positiveButton, String negativeButton, CallbackRaiingAlertDialog allbackRaiingAlertDialog) {
        if (activity == null) {
            throw new RuntimeException("activity不能为空");
        }
        if (message == null && title == null) {
            throw new RuntimeException("message和title不能同时为空");
        }
        if (positiveButton == null && negativeButton == null) {
            throw new RuntimeException("确定或取消按钮不能同时为空");
        }
        dialog = new MyAlertDialog(activity, title, message, positiveButton, negativeButton, allbackRaiingAlertDialog);
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

    /**
     * 判断对话框是否正在显示
     */
    public boolean isShow() {
        if (dialog != null && dialog.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * 点击按钮后的回调
     */
    public interface CallbackRaiingAlertDialog {
        /**
         * 点击确定
         */
        void onPositive();

        /**
         * 点击取消,或者主动cancel掉
         */
        void onNegative();
    }

    private class MyAlertDialog extends AlertDialog implements View.OnClickListener {

        public CallbackRaiingAlertDialog mCallbackRaiingAlertDialog;
        /**
         * 标题  字符
         */
        private TextView dialog_alert_title;
        /**
         * 标题和正文之间的空白 布局
         */
        private View dialog_title_content_space;
        /**
         * 内容 字符
         */
        private TextView dialog_alert_message;
        /**
         * 确定和取消按钮的布局
         */
        private View dialog_alert_all_layout;
        /**
         * 取消按钮(界面只有取消按钮)
         */
        private Button dialog_alert_cancel1;
        /**
         * 取消按钮(确定和取消按钮同时存在)
         */
        private Button dialog_alert_cancel2;
        /**
         * 确定按钮(界面只有确定按钮)
         */
        private Button dialog_alert_ok1;
        /**
         * 确定按钮(确定和取消按钮同时存在)
         */
        private Button dialog_alert_ok2;
        /**
         * 标题(为null时,表示没有标题，但是标题或者正文必须有一个，否则会崩溃)
         */
        private String mTitle = null;
        /**
         * 内容(为null时,表示没有正文，但是标题或者正文必须有一个，否则会崩溃)
         */
        private String mMessage = null;
        /**
         * 确定按钮的text(确定或取消按钮必有其一,否则崩溃)
         */
        private String mPositiveButton = null;
        /**
         * 取消按钮的text(确定或取消按钮必有其一,否则崩溃)
         */
        private String mNegativeButton = null;
        private Activity mActivity;

        /**
         * 构造器
         *
         * @param activity                  活动
         * @param title                     标题（可为空，但是标题和正文必须存在一个）
         * @param message                   正文（可为空，但是标题和正文必须存在一个）
         * @param positiveButton            取消按钮（确定或取消按钮必须存在一个）（取消有字体加粗效果）
         * @param negativeButton            确认按钮（确定或取消按钮必须存在一个）
         * @param callbackRaiingAlertDialog 点击手势的回调
         */
        protected MyAlertDialog(Activity activity, String title, String message, String positiveButton, String negativeButton, CallbackRaiingAlertDialog callbackRaiingAlertDialog) {
            super(activity);

            mActivity = activity;
            mTitle = title;
            mMessage = message;
            mPositiveButton = positiveButton;
            mNegativeButton = negativeButton;
            mCallbackRaiingAlertDialog = callbackRaiingAlertDialog;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_alert);
            initLayout();
            initData();
        }

        /**
         * 初始化视图
         */
        private void initLayout() {
            dialog_alert_all_layout = findViewById(R.id.dialog_alert_all_layout);
            dialog_alert_title = (TextView) findViewById(R.id.dialog_alert_title);
            dialog_title_content_space = findViewById(R.id.dialog_title_content_space);
            dialog_alert_message = (TextView) findViewById(R.id.dialog_alert_message);
            dialog_alert_cancel1 = (Button) findViewById(R.id.dialog_alert_cancle1);
            dialog_alert_cancel2 = (Button) findViewById(R.id.dialog_alert_cancle2);
            dialog_alert_ok1 = (Button) findViewById(R.id.dialog_alert_ok1);
            dialog_alert_ok2 = (Button) findViewById(R.id.dialog_alert_ok2);
            dialog_alert_cancel1.setOnClickListener(this);
            dialog_alert_cancel2.setOnClickListener(this);
            dialog_alert_ok1.setOnClickListener(this);
            dialog_alert_ok2.setOnClickListener(this);
        }

        /**
         * 初始化数据
         */
        private void initData() {
            // 如果不需要显示标题，则隐藏
            if (mTitle == null) {
                dialog_alert_title.setVisibility(View.GONE);
                dialog_title_content_space.setVisibility(View.GONE);
            } else {
                dialog_alert_title.setVisibility(View.VISIBLE);
                dialog_title_content_space.setVisibility(View.VISIBLE);
                dialog_alert_title.setText(mTitle);
            }

            // 如果不需要显示正文，则隐藏
            if (mMessage == null) {
                dialog_alert_message.setVisibility(View.GONE);
                dialog_title_content_space.setVisibility(View.GONE);
            } else {
                dialog_alert_message.setVisibility(View.VISIBLE);
                dialog_title_content_space.setVisibility(View.VISIBLE);
                dialog_alert_message.setText(mMessage);
            }

            // 根据按钮选择布局
            if (mPositiveButton != null && mNegativeButton != null) {
                // 既有"取消"也有"确定"
                dialog_alert_ok1.setVisibility(View.GONE);
                dialog_alert_cancel1.setVisibility(View.GONE);
                dialog_alert_all_layout.setVisibility(View.VISIBLE);
                dialog_alert_cancel2.setText(mNegativeButton);
                dialog_alert_ok2.setText(mPositiveButton);
            } else if (mPositiveButton != null) {
                // 只有"确认"按钮
                dialog_alert_all_layout.setVisibility(View.GONE);
                dialog_alert_cancel1.setVisibility(View.GONE);
                dialog_alert_ok1.setVisibility(View.VISIBLE);
                dialog_alert_ok1.setText(mPositiveButton);
            } else if (mNegativeButton != null) {
                // 只有"取消"按钮
                dialog_alert_all_layout.setVisibility(View.GONE);
                dialog_alert_ok1.setVisibility(View.GONE);
                dialog_alert_cancel1.setVisibility(View.VISIBLE);
                dialog_alert_cancel1.setText(mNegativeButton);
            }

            // 设置点击
            setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (mCallbackRaiingAlertDialog != null) {
                        mCallbackRaiingAlertDialog.onNegative();
                        Log.d("", "dialog  cancel ");
                    }
                }
            });
//            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.dialog_alert_cancle1) {
                MyAlertDialog.this.cancel();
            } else if (view.getId() == R.id.dialog_alert_cancle2) {
                MyAlertDialog.this.cancel();
            } else if (view.getId() == R.id.dialog_alert_ok1) {
                MyAlertDialog.this.dismiss();
                if (mCallbackRaiingAlertDialog != null) {
                    mCallbackRaiingAlertDialog.onPositive();
                }
            } else if (view.getId() == R.id.dialog_alert_ok2) {
                MyAlertDialog.this.dismiss();
                if (mCallbackRaiingAlertDialog != null) {
                    mCallbackRaiingAlertDialog.onPositive();
                }
            }
        }

        @Override
        public void show() {
            super.show();
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            int screenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;
            params.width = (int) ((double) screenWidth / 4.0 * 2.8);
            window.setAttributes(params);
            window.setGravity(Gravity.CENTER);
        }
    }

}

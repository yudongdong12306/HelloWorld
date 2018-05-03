package com.gsh.dialoglibrary;

/**
 * Created by damon on 6/18/15.
 * 通用Toast
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 布丁自定义的吐司类
 */
public class RaiingToast {
    /**
     * message id
     */
    private static final int MESSAGE_TIMEOUT = 2;
    /**
     * 当Toast处于显示状态时,不在执行Toast了
     */
    public static boolean isShow;
    private static TextView textView;
    private static WorkerHandler mHandler;
    private static Timer timer;
    private static TimerTask task;
    private WindowManager wdm;
    private double time;
    private View mView;
    private WindowManager.LayoutParams params;
    private Toast toast;
    private Object mTN;
    private Method show;
    private Method hide;

    private RaiingToast(Context context, String text, double time) {
        this.time = time;
        if (isShow) {
            textView.setText(text);
            return;
        }
        wdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mHandler = new WorkerHandler();

//        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast = new Toast(context);
        //居中
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        mView = LayoutInflater.from(context).inflate(R.layout.toastview, null, false);
        textView = (TextView) mView.findViewById(R.id.toastview_text);
        textView.setText(text);
        toast.setView(mView);

//        mView = toast.getView();
    }

    public static RaiingToast makeText(Context context, String text, double time) {
        RaiingToast toastCustom = new RaiingToast(context, text, time);
        return toastCustom;
    }

    public void show() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
//                    Log.d("", "发送handler"+task.hashCode());
                    //这里mHandler是由可呢为空的
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(MESSAGE_TIMEOUT);
                    }
                }
            };
        }
        if (timer != null) {
//                    Log.d("", "启动task的hashcode-->>"+task.hashCode());
            timer.schedule(task, (long) (time * 1000));
        }
        if (isShow) {
            return;
        }

        initTN();
        //调用show方法来吐司
        try {
            //7.1手机这里show为null导致崩溃,做下判断,如果为null的话就直接调用原生方法toast.show()
            if (show == null) {
                toast.show();
            } else {
                show.invoke(mTN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("RaiingToast", "show: params: " + params);
        //7.1手机这里params为null导致崩溃,做下判断,如果为null的话就不调用addView方法了,因为上面已经调用原生方法了
        if (params != null) {
            wdm.addView(mView, params);
        }
        isShow = true;
//        mHandler.sendEmptyMessageDelayed(MESSAGE_TIMEOUT, (long) (time * 1000));
    }

    public void cancel() {
        if (!isShow) {
            return;
        }
        isShow = false;
        timer = null;
        task = null;
        textView = null;
        mHandler = null;
        try {
            wdm.removeView(mView);
        } catch (Exception e) {
            e.printStackTrace();
            //避免内存泄露.不清楚为什么内存泄露了
        }

    }

    /**
     * 通过反射机制获取TN类的关键方法,show,hide
     * 以及字段mParams,mNextView.并且给字段赋值
     * （该类通过反射机制获取Toast中的mTN以及TN类中的show吐司和取消吐司的方法.绕过小米系统的权限检验.）
     */
    private void initTN() {
        try {
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(toast);
            show = mTN.getClass().getMethod("show");
            hide = mTN.getClass().getMethod("hide");

            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
//        params.windowAnimations = toast.getView().getAnimation().INFINITE;
            ScaleAnimation sa = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            params.windowAnimations = R.style.custom_toast_anim_view;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

            /**调用tn.show()之前一定要先设置mNextView*/
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());
//            WindowManager mWM = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class WorkerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TIMEOUT:
                    cancel();
                    break;
            }
        }
    }
}



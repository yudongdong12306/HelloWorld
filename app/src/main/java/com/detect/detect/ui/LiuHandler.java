package com.detect.detect.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class LiuHandler extends Handler {

    private IHandleMessage mIHandleMessage;
    private WeakReference<Context> weakReference;

    public LiuHandler(Context context, IHandleMessage iHandleMessage) {
        super(Looper.getMainLooper());
        this.weakReference = new WeakReference<>(context);
        this.mIHandleMessage = iHandleMessage;
    }

    @Override
    public void handleMessage(Message msg) {
        Context context = weakReference.get();
        if (context != null) {
            mIHandleMessage.handleMessage(msg);
        }
    }


}

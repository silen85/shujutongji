package com.lesso.data.common;

import android.content.Context;
import android.util.Log;

/**
 * Created by meisl on 2015/7/16.
 */
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String TAG = "com.lesso.data.common.DefaultExceptionHandler";

    private Context context = null;

    public DefaultExceptionHandler(Context context) {
        this.context = context;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        // 收集异常信息 并且发送到服务器
        sendCrashReport(ex);

        // 处理异常
        handleException();
    }


    private void sendCrashReport(Throwable ex) {


        StringBuffer exceptionStr = new StringBuffer();

        exceptionStr.append(ex.getMessage());


        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString());
        }


        //TODO
        //发送收集到的Crash信息到服务器

        Log.e(TAG, ex.getMessage(), ex);


    }


    private void handleException() {

        //TODO

        //这里可以对异常进行处理。

        //比如提示用户程序崩溃了。

        //比如记录重要的信息，尝试恢复现场。

        //或者干脆记录重要的信息后，直接杀死程序。

        android.os.Process.killProcess(android.os.Process.myPid());

    }


}
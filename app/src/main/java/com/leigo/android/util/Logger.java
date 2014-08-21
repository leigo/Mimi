package com.leigo.android.util;

import android.util.Log;

/**
 * Created by Administrator on 2014/8/21.
 */
public class Logger {

    private final String tag;

    public Logger(Class<?> tag) {
        this.tag = tag.getName();
    }

    public void d(Object paramObject) {
        Log.d(this.tag, paramObject.toString());
    }

    public void d(Object msg, Throwable tr) {
        Log.d(this.tag, msg.toString(), tr);
    }

    public void d(Throwable tr) {
        d("", tr);
    }

    public void e(Object msg) {
        Log.e(this.tag, msg.toString());
    }

    public void e(Object msg, Throwable tr) {
        Log.e(this.tag, msg.toString(), tr);
    }

    public void e(Throwable tr) {
        e("", tr);
    }

    public void i(Object msg) {
        Log.i(this.tag, msg.toString());
    }

    public void i(Object msg, Throwable tr) {
        Log.i(this.tag, msg.toString(), tr);
    }

    public void i(Throwable tr) {
        i("", tr);
    }

    public void w(Object msg) {
        Log.w(this.tag, msg.toString());
    }

    public void w(Object msg, Throwable tr) {
        Log.w(this.tag, msg.toString(), tr);
    }

    public void w(Throwable tr) {
        w("", tr);
    }
}

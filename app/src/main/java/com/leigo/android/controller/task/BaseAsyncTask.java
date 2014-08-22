package com.leigo.android.controller.task;

import android.content.Context;
import android.os.Handler;

import com.leigo.android.util.Logger;
import com.leigo.android.util.NamedThreadFactory;

import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import roboguice.util.RoboAsyncTask;

/**
 * Created by Administrator on 2014/8/22.
 */
public abstract class BaseAsyncTask<T> extends RoboAsyncTask<T> {

    public static final String ERROR_LOG_FILE_PREFIX = "log/error.log.";

    private static final ExecutorService FILE_LOGGER_EXECUTOR = Executors.newSingleThreadExecutor(new NamedThreadFactory("file-logger") {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = super.newThread(r);
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    });

    protected BaseAsyncTask(Context context) {
        super(context);
    }

    protected BaseAsyncTask(Context context, Handler handler) {
        super(context, handler);
    }

    protected BaseAsyncTask(Context context, Handler handler, Executor executor) {
        super(context, handler, executor);
    }

    protected BaseAsyncTask(Context context, Executor executor) {
        super(context, executor);
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        if ((e instanceof ClosedByInterruptException)) {
            onInterrupted(e);
        }
        onOwnException(e);
    }

    protected void onOwnException(Exception e)
            throws RuntimeException {
    }
}

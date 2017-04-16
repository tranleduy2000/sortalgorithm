/*
 * Copyright (C) 2016 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.duy.algorithm.algorithms;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.duy.algorithm.SortCompletionListener;
import com.duy.algorithm.customview.LogView;

import java.util.concurrent.atomic.AtomicBoolean;

public class AlgorithmThread extends HandlerThread {
    public static final String KEY_ALGORITHM = "KEY_ALGORITHM";
    public static final String COMMAND_START_ALGORITHM = "start";
    private final static String TAG = "AlgorithmThread";
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final Object pauseLock = new Object();
    protected Activity activity;
    protected long delayTime = 50;
    private SortCompletionListener completionListener;
    private boolean started;
    private Handler mHandler;
    private LogView mLogger;

    public AlgorithmThread() {
        super("");
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public void sleep() {
        sleepFor(delayTime);
    }

    public void sleepFor(long time) {
        try {
            sleep(time);
            if (isPaused())
                pauseExecution();
            else resumeExecution();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public String getString(int resID){
        return activity.getString(resID);
    }

    public void startExecution() {
        started = true;
        sleepFor(delayTime * 2);
    }

    /**
     * pause thread
     */
    private void pauseExecution() {
        if (paused.get()) {
            synchronized (getPauseLock()) {
                if (paused.get()) {
                    try {
                        getPauseLock().wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    /**
     * resume thread
     */
    private void resumeExecution() {
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    private Object getPauseLock() {
        return pauseLock;
    }

    public boolean isPaused() {
        return paused.get();
    }

    public void setPaused(boolean b) {
        paused.set(b);
        if (!b) {
            synchronized (getPauseLock()) {
                getPauseLock().notify();
            }
        }
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void showLog(final String log) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLogger != null)
                    mLogger.addLog(log);
            }
        });
    }

    public void showLog(final String message, final int[] array) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLogger != null)
                    mLogger.addLog(message, array.clone());

            }
        });
    }

    public void setCompletionListener(SortCompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    public void prepareHandler(final IDataHandler dataHandler) {
        mHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj instanceof String) {
                    dataHandler.onMessageReceived((String) msg.obj);
                } else {
                    dataHandler.onDataReceived(msg.obj);
                }
                return true;
            }
        });

    }

    /**
     * send data
     *
     * @param data
     */
    public void sendData(Object data) {
        mHandler.obtainMessage(1, data).sendToTarget();
    }

    /**
     * send command
     *
     * @param message
     */
    public void sendMessage(String message) {
        mHandler.obtainMessage(1, message).sendToTarget();
    }

    /**
     * completed sort
     */
    public void onCompleted() {
        if (completionListener != null) completionListener.onSortCompleted();
        started = false;
    }

    public void setLogger(LogView mLogger) {
        this.mLogger = mLogger;
    }

    public static final class ALGORITHM_NAME {
        public static final String BUBBLE_SORT = "BUBBLE_SORT";
        public static final String INSERTION_SORT = "INSERTION_SORT";
        public static final String SELECTION_SORT = "SELECTION_SORT";
        public static final String QUICK_SORT = "QUICK_SORT";
        public static final String SHELL_SORT = "SHELL_SORT";
        public static final String MERGE_SORT = "MERGE_SORT";
        public static final String COCKTAIL_SORT = "COCKTAIL_SORT";
        public static final String BOGO_SORT = "BOGO_SORT";
        public static final String COUNTING_SORT = "COUNTING_SORT";
    }
}

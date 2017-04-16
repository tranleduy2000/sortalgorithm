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


import android.support.annotation.UiThread;

import com.duy.algorithm.customview.SortView;

public class SortAlgorithmThread extends AlgorithmThread implements IDataHandler {

    public static final String TAG = SortAlgorithmThread.class.getSimpleName();
    public SortView mSortView;
    private boolean isPrepared = false;
    private boolean swapAnimateEnable = true;

    public void prepare() {
        if (isPrepared) return;
        start();
        prepareHandler(this);
        isPrepared = true;
    }

    /**
     * set array data for view
     *
     * @param array
     */
    @UiThread
    public void setData(final int[] array) {
        prepare();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSortView.setCompletePosition(-1);
                mSortView.setArray(array);
                mSortView.setTime(0);
                mSortView.invalidate();
            }
        });
        sendData(array);
    }

    /**
     * highlight swap
     */
    @UiThread
    public void onSwapping(final int one, final int two) {

        if (swapAnimateEnable) { //some cases without effect
            mSortView.setSwapPosition(one, two, false);
            //xA always smaller xB
            int delta = mSortView.getDelta();
            long timeSleep;
            if (delta == 0) {
                timeSleep = 1;
            } else {
                timeSleep = (long) (delayTime * 1.5 / delta);
            }
            while (delta >= 0) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSortView.incPositionSwap(2);
                    }
                });
                sleepFor(timeSleep);
                delta -= 2;
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSortView.setSwapPosition(one, two);
                }
            });
            sleep();
        }
    }

    /**
     * highlight swap
     */
    @UiThread
    public void onSwapped() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSortView.setSwapPosition(-1, -1);
            }
        });
        sleep();
    }

    /**
     * highlight swap
     */
    @UiThread
    public void onSwapped(boolean b) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSortView.setSwapPosition(-1, -1);
            }
        });
    }

    /**
     * highlight trace
     *
     * @param position - position for trace
     */
    public void onTrace(final int position) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSortView.setTracePosition(position);
            }
        });
    }

    /**
     * highlight target
     *
     * @param position-position for target
     */
    public void onTarget(final int position) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSortView.setTargetPosition(position);
            }
        });
    }

    @Override
    public void onDataReceived(Object data) {

    }

    @Override
    public void onMessageReceived(String message) {

    }

    /**
     * array is sorted
     */
    @Override
    public void onCompleted() {
        finishSorting();
        super.onCompleted();
    }


    private void finishSorting() {
        for (int i = 0; i < mSortView.getSizeArray(); i++) {
            mSortView.setCompletePosition(i);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSortView.invalidate();
                }
            });
            sleepFor(delayTime / 3);
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mSortView.setCompletePosition(-1);
                mSortView.setTracePosition(-1);
                mSortView.invalidate();
            }
        });
    }

    @Override
    public void sleep() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mSortView.setCompletePosition(-1);
                mSortView.addTimeUnit(1);
                mSortView.invalidate();
            }
        });
        super.sleep();
    }

    public boolean isSwapAnimateEnable() {
        return swapAnimateEnable;
    }

    public void setSwapAnimateEnable(boolean swapAnimateEnable) {
        this.swapAnimateEnable = swapAnimateEnable;
    }
}

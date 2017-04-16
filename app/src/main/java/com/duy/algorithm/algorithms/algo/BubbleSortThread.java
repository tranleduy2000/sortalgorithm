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

package com.duy.algorithm.algorithms.algo;


import android.app.Activity;

import com.duy.algorithm.R;
import com.duy.algorithm.algorithms.AlgorithmThread;
import com.duy.algorithm.algorithms.SortAlgorithmThread;
import com.duy.algorithm.customview.SortView;

public class BubbleSortThread extends SortAlgorithmThread {

    int[] array;

    public BubbleSortThread(SortView sortView, Activity activity) {
        this.mSortView = sortView;
        this.activity = activity;
    }

    public void sort() {
        showLog(getString(R.string.original_arr), array);

        for (int i = array.length - 1; i >= 0; i--) {
//            showLog("i = " + i);
            boolean swapped = false;
            for (int j = 0; j < i; j++) {
                //handle trace
                onTrace(j);
                sleep();

                if (array[j] > array[j + 1]) {
                    //swapping
                    onSwapping(j, j + 1);

                    showLog("Swapping a[" + i + "]=" + array[j] + " and a[" + j + "]=" + array[j + 1]);
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    //swapped
                    onSwapped();
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        showLog(getString(R.string.arr_sorted), array);

        onCompleted();
    }

    @Override
    public void run() {
        super.run();
    }


    @Override
    public void onDataReceived(Object data) {
        super.onDataReceived(data);
        this.array = (int[]) data;
    }

    @Override
    public void onMessageReceived(String message) {
        super.onMessageReceived(message);
        if (message.equals(AlgorithmThread.COMMAND_START_ALGORITHM)) {
            startExecution();
            sort();
        }
    }
}

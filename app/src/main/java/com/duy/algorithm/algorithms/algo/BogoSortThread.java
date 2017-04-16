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
import com.duy.algorithm.utils.ArrayUtils;

public class BogoSortThread extends SortAlgorithmThread {

    int[] array;

    public BogoSortThread(SortView sortView, Activity activity) {
        this.mSortView = sortView;
        this.activity = activity;
    }


    public boolean bogoIsSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < arr[i - 1])
                return false;
        return true;
    }

    public void sort() {
        showLog("Original array - ", array);

        while (!bogoIsSorted(array)) {
            for (int i = 0; i < array.length; i++) {
                showLog("Doing iteration - " + i);


                int j = (int) (Math.random() * array.length);
                onSwapping(i, j);
                sleep();
                showLog("Swapping " + array[i] + " and " + array[j]);

                ArrayUtils.swap(array, i, j);

                onSwapping(i, j);
                sleep();
            }
            //sleep(100.0);
        }
        showLog(getString(R.string.arr_sorted), array);
        onCompleted();
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

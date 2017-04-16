package com.duy.algorithm.algorithms.algo;


import android.app.Activity;

import com.duy.algorithm.R;
import com.duy.algorithm.algorithms.AlgorithmThread;
import com.duy.algorithm.algorithms.SortAlgorithmThread;
import com.duy.algorithm.customview.SortView;

public class ShellSortThread extends SortAlgorithmThread {

    int[] array;

    public ShellSortThread(SortView sortView, Activity activity) {
        this.mSortView = sortView;
        this.activity = activity;
    }

    public void sort() {
        showLog(getString(R.string.original_arr), array);


        int h = 1;
        while (h < array.length) h = 3 * h + 1;
        while (h > 0) {
            h = h / 3;
            for (int k = 0; k < h; k++) {
                for (int i = h + k; i < array.length; i += h) {
                    onTrace(i);
                    sleep();

                    int key = array[i];
                    int j = i - h;

                    while (j >= 0 && array[j] > key) {
                        onSwapping(j, j + h);
                        sleep();

                        array[j + h] = array[j];
                        onSwapped(false);
                        j -= h;
                    }
                    array[j + h] = key;
                    //-> invariant: array[0,h,2*h..j] is sorted
                }
            }
            //->invariant: each h-sub-array is sorted
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

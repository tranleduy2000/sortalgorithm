package com.duy.algorithm.algorithms.algo;

import android.app.Activity;

import com.duy.algorithm.R;
import com.duy.algorithm.algorithms.AlgorithmThread;
import com.duy.algorithm.algorithms.SortAlgorithmThread;
import com.duy.algorithm.customview.SortView;
import com.duy.algorithm.utils.ArrayUtils;

public class QuickSortThread extends SortAlgorithmThread {
    public static final String TAG = "QuickSort";
    private int[] array;

    public QuickSortThread(SortView sortView, Activity activity) {
        this.mSortView = sortView;
        this.activity = activity;
    }

    private void sort(int l, int r) {
        int i = l;
        int j = r;
        int key = array[(l + r) / 2];
        while (i <= j) {
            while (array[i] < key) {
                onTrace(i);
                sleep();
                i++;
            }
            while (array[j] > key) {
                onTrace(j);
                sleep();
                j--;
            }
            if (i <= j) {
                if (array[i] > array[j]) {
                    onSwapping(i, j);

                    ArrayUtils.swap(array, i, j);

                    //swapped, show it
                    onSwapped();
                }
                i++;
                j--;
            }
        }
        if (i < r) sort(i, r);
        if (l < j) sort(l, j);
    }

    public void sort() {
        showLog(getString(R.string.original_arr), array);

        sort(0, array.length - 1);
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

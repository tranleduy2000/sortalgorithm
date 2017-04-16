package com.duy.algorithm.algorithms.algo;

import android.app.Activity;

import com.duy.algorithm.R;
import com.duy.algorithm.algorithms.AlgorithmThread;
import com.duy.algorithm.algorithms.SortAlgorithmThread;
import com.duy.algorithm.customview.SortView;

/**
 * Created by amit on 21/11/16.
 */

public class SelectionSortThread extends SortAlgorithmThread {

    int[] array;

    public SelectionSortThread(SortView sortView, Activity activity) {
        this.mSortView = sortView;
        this.activity = activity;
    }

    public void sort() {
        showLog(getString(R.string.original_arr), array);


        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            onTarget(i);
            sleep();

            for (int j = i + 1; j < n; j++) {
                onTrace(j);
                sleep();

                if (array[j] < array[min_idx]) {
                    min_idx = j;
                    onTarget(j);
                }
            }

            //swapping
            onTarget(min_idx);
            onSwapping(min_idx, i);

            int temp = array[min_idx];
            array[min_idx] = array[i];
            array[i] = temp;

            showLog("Swapping " + array[i] + " and " + temp);

            //swapped
            onTarget(i);
            onSwapped();


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

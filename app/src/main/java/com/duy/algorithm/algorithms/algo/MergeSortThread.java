package com.duy.algorithm.algorithms.algo;

import android.app.Activity;

import com.duy.algorithm.R;
import com.duy.algorithm.algorithms.AlgorithmThread;
import com.duy.algorithm.algorithms.SortAlgorithmThread;
import com.duy.algorithm.customview.SortView;

public class MergeSortThread extends SortAlgorithmThread {

    private int[] array;
    private int[] aux;

    public MergeSortThread(SortView sortView, Activity activity) {
        this.mSortView = sortView;
        this.activity = activity;
    }

    private void merge(int lo, int mid, int hi) {
        int l = lo;
        int r = mid + 1;

        System.arraycopy(array, lo, aux, lo, hi + 1 - lo);

        for (int k = l; k <= hi; k++) {
            onTrace(k);
            sleep();
            int rec = 0;
            if (l > mid) {
                rec = r;
                array[k] = aux[r++];
            } else if (r > hi) {
                rec = l;
                array[k] = aux[l++];
            } else if (aux[r] < aux[l]) {
                rec = r;
                array[k] = aux[r++];
            } else {
                rec = l;
                array[k] = aux[l++];
            }

//            onSwapping(rec, k);
//            sleep();
        }


    }

    private void sort(int lo, int hi) {

        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(lo, mid);          // 将左半部分排序
        sort(mid + 1, hi);          // 将又半部分排序
        merge(lo, mid, hi);     // 归并结果

    }


    public void sort() {
        showLog(getString(R.string.original_arr), array);

        aux = new int[array.length];
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

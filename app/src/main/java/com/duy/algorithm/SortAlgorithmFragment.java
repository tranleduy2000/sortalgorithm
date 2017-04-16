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

package com.duy.algorithm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duy.algorithm.algorithms.SortAlgorithmThread;
import com.duy.algorithm.algorithms.algo.BubbleSortThread;
import com.duy.algorithm.algorithms.algo.CocktailShakerSortThread;
import com.duy.algorithm.algorithms.algo.InsertionSortThread;
import com.duy.algorithm.algorithms.algo.MergeSortThread;
import com.duy.algorithm.algorithms.algo.QuickSortThread;
import com.duy.algorithm.algorithms.algo.SelectionSortThread;
import com.duy.algorithm.algorithms.algo.ShellSortThread;
import com.duy.algorithm.customview.LogView;
import com.duy.algorithm.customview.SortView;
import com.duy.algorithm.utils.ArrayUtils;

import java.util.regex.Pattern;

import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.BUBBLE_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.COCKTAIL_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.INSERTION_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.MERGE_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.QUICK_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.SELECTION_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.SHELL_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.COMMAND_START_ALGORITHM;
import static com.duy.algorithm.algorithms.AlgorithmThread.KEY_ALGORITHM;

public class SortAlgorithmFragment extends AbstractSortAlgorithmFragment {

    private final static String TAG = SortAlgorithmFragment.class.getSimpleName();
    protected SortAlgorithmThread thread;
    private SortView mSortView;
    private int mCountData = 50;
    private LogView mLogger;

    public static SortAlgorithmFragment newInstance(String algorithm) {
        SortAlgorithmFragment fragment = new SortAlgorithmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ALGORITHM, algorithm);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

    @Override
    protected void createSortView() {
        mSortView = (SortView) findViewById(R.id.sortView);
        mLogger = (LogView) findViewById(R.id.rc_log);
        mLogger.setmEmptyView(findViewById(R.id.empty_view));
    }

    @Override
    public void setupFragment(String algorithmKey) {
        switch (algorithmKey) {
            case BUBBLE_SORT:
                thread = new BubbleSortThread(mSortView, getActivity());
                break;
            case INSERTION_SORT:
                thread = new InsertionSortThread(mSortView, getActivity());
                break;
            case SELECTION_SORT:
                thread = new SelectionSortThread(mSortView, getActivity());
                break;
            case QUICK_SORT:
                thread = new QuickSortThread(mSortView, getActivity());
                break;
            case MERGE_SORT:
                thread = new MergeSortThread(mSortView, getActivity());
                break;
            case SHELL_SORT:
                thread = new ShellSortThread(mSortView, getActivity());
                break;
            case COCKTAIL_SORT:
                thread = new CocktailShakerSortThread(mSortView, getActivity());
                break;
        }
        thread.setStarted(false);
        thread.setCompletionListener(this);
        thread.setLogger(mLogger);
        generateRandomData();
    }

    @Override
    public void enableView() {
        mEditArray.setEnabled(true);
        mEditNumber.setEnabled(true);
        btnRandom.setEnabled(true);
    }


    @Override
    protected void setDelayTime(int value) {
        if (thread.isStarted())
            thread.setDelayTime(value + 10);
    }

    @Override
    protected void startSort() {
        if (createData()) {
            thread.sendMessage(COMMAND_START_ALGORITHM);
            mPlay.setImageResource(R.drawable.ic_pause_white_24dp);
            mPlayAnimation.show();
            disableView();
        }
    }

    @Override
    protected void fabClick() {
        if (!thread.isStarted()) {//If you have sorted or not sorted
            startSort();
        } else {   //if it is sorting, pause or resume thread
            if (thread.isPaused())
                resumeSort();
            else
                pauseSort();
        }
    }


    @Override
    public void generateRandomData() {
        //check empty length of array
        if (mEditNumber.getText().toString().isEmpty()) {
            mCountData = Integer.parseInt(mEditNumber.getHint().toString());
        } else {
            mCountData = Integer.parseInt(mEditNumber.getText().toString());
        }

        int[] array = ArrayUtils.createIntArray(mCountData);
        thread.setData(array);
        mEditArray.setText(ArrayUtils.arrayToString(array));
    }

    @Override
    public boolean createData() {
        Log.d(TAG, "createData: ");

        //set time sleep for thread
        int delayTime = mTimeBar.getProgress();
        thread.setDelayTime(delayTime);

        //check empty length of array
        if (mEditNumber.getText().toString().isEmpty()) {
            mCountData = Integer.parseInt(mEditNumber.getHint().toString());
        } else {
            mCountData = Integer.parseInt(mEditNumber.getText().toString());
        }

        String raw = mEditArray.getText().toString();
        final String strArray[] = raw.split(Pattern.quote(","));
        if (strArray.length == mCountData) {
            int[] array = ArrayUtils.arrayStringToInt(strArray);

            //If receive an error during conversion
            if (array[0] == -1) {
                return false;
            }
            thread.setData(array);
            return true;
        } else {
//            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            if (strArray.length > mCountData) {
                Toast.makeText(getActivity(), "Please remove " +
                        +(strArray.length - mCountData)
                        + " entry of array, length of array is "
                        + strArray.length, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Please add "
                        + (-strArray.length + mCountData)
                        + " entry of array, length of array is "
                        + strArray.length, Toast.LENGTH_LONG).show();
            }

            return false;
        }
    }

    @Override
    public void pauseSort() {
        if (thread.isStarted()) {
            thread.setPaused(true);
            mPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            mPlayAnimation.hide();
            disableView();
        }
    }

    @Override
    protected void resumeSort() {
        if (thread.isPaused()) {
            thread.setPaused(false);
            mPlay.setImageResource(R.drawable.ic_pause_white_24dp);
            mPlayAnimation.show();
            disableView();
        }
    }

    @Override
    public void onSortCompleted() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.sorted, Toast.LENGTH_SHORT).show();
                    enableView();
                    mPlayAnimation.hide();
                    mPlay.setImageResource(R.drawable.ic_settings_backup_restore_white_24dp);
                }
            });
        }
        super.onSortCompleted();
    }
}

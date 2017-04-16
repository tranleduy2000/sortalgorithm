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

import com.duy.algorithm.algorithms.AlgorithmThread;
import com.duy.algorithm.algorithms.SortAlgorithmThread;
import com.duy.algorithm.algorithms.algo.BubbleSortThread;
import com.duy.algorithm.algorithms.algo.CocktailShakerSortThread;
import com.duy.algorithm.algorithms.algo.InsertionSortThread;
import com.duy.algorithm.algorithms.algo.MergeSortThread;
import com.duy.algorithm.algorithms.algo.QuickSortThread;
import com.duy.algorithm.algorithms.algo.SelectionSortThread;
import com.duy.algorithm.algorithms.algo.ShellSortThread;
import com.duy.algorithm.customview.SortView;
import com.duy.algorithm.utils.ArrayUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.regex.Pattern;

import static com.duy.algorithm.algorithms.AlgorithmThread.KEY_ALGORITHM;

public class SortSpeedTestFragmentSort extends AbstractSortAlgorithmFragment {
    private final static String TAG = SortSpeedTestFragmentSort.class.getSimpleName();
    private final int mCount = 7;
    private SortAlgorithmThread[] threads = new SortAlgorithmThread[mCount];
    private SortView[] mSortViews = new SortView[mCount];
    private int mCountData = 50;
    private int numThreadCompleted = 0;

    public static SortSpeedTestFragmentSort newInstance(String algorithm) {
        SortSpeedTestFragmentSort fragment = new SortSpeedTestFragmentSort();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ALGORITHM, algorithm);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup v) {
        return inflater.inflate(R.layout.fragment_multi_sort, v, false);
    }

    @Override
    protected void createSortView() {
        mSortViews[0] = (SortView) findViewById(R.id.sortView0);
        mSortViews[0].setName(R.string.bubble_sort);

        mSortViews[1] = (SortView) findViewById(R.id.sortView1);
        mSortViews[1].setName(R.string.insert_sort);

        mSortViews[2] = (SortView) findViewById(R.id.sortView2);
        mSortViews[2].setName(R.string.selection_sort);

        mSortViews[3] = (SortView) findViewById(R.id.sortView3);
        mSortViews[3].setName(R.string.quick_sort);

        mSortViews[4] = (SortView) findViewById(R.id.sortView4);
        mSortViews[4].setName(R.string.merge_sort);

        mSortViews[5] = (SortView) findViewById(R.id.sortView5);
        mSortViews[5].setName(R.string.shell_sort);

        mSortViews[6] = (SortView) findViewById(R.id.sortView6);
        mSortViews[6].setName(R.string.cocktail_sort);
    }

    @Override
    protected void setDelayTime(int value) {
        for (AlgorithmThread thread : threads) {
            thread.setDelayTime(value + 10);
        }
    }

    @Override
    protected void fabClick() {
        boolean ended = true;

        for (AlgorithmThread thread : threads) {
            if (thread.isStarted()) {
                ended = false;
                break;
            }
        }

        if (ended) {//If you have sorted or not sorted
            startSort();
        } else {   //if it is sorting, pause or resume threads
            boolean isAllPause = true;
            for (AlgorithmThread thread : threads) {
                if (!thread.isPaused()) {
                    isAllPause = false;
                    break;
                }
            }
            if (isAllPause) {
                resumeSort();
            } else {
                pauseSort();
            }
        }
    }

    protected void startSort() {
        if (createData()) {
            numThreadCompleted = 0;
            for (AlgorithmThread thread : threads) {
                thread.sendMessage(AlgorithmThread.COMMAND_START_ALGORITHM);
            }
            mPlay.setImageResource(R.drawable.ic_pause_white_24dp);
            mPlayAnimation.show();
            disableView();
        }
    }

    @Override
    public void setupFragment(String name) {
        mEditNumber.setText("50");
        mTimeBar.setProgress(100);

        threads[0] = new BubbleSortThread(mSortViews[0], getActivity());
        threads[1] = new InsertionSortThread(mSortViews[1], getActivity());
        threads[2] = new SelectionSortThread(mSortViews[2], getActivity());
        threads[3] = new QuickSortThread(mSortViews[3], getActivity());
        threads[4] = new MergeSortThread(mSortViews[4], getActivity());
        threads[5] = new ShellSortThread(mSortViews[5], getActivity());
        threads[6] = new CocktailShakerSortThread(mSortViews[6], getActivity());
        //disable animate
        for (SortAlgorithmThread thread : threads) {
            thread.setSwapAnimateEnable(false);
            thread.setCompletionListener(this);

        }
        generateRandomData();
    }

    @Override
    protected void generateRandomData() {
        //check empty length of array
        if (mEditNumber.getText().toString().isEmpty()) {
            mCountData = Integer.parseInt(mEditNumber.getHint().toString());
        } else {
            mCountData = Integer.parseInt(mEditNumber.getText().toString());
        }

        int[] array = ArrayUtils.createIntArray(mCountData);
        for (SortAlgorithmThread thread : threads) {
            thread.setData(array.clone());
        }
        mEditArray.setText(ArrayUtils.arrayToString(array));
    }


    @Override
    public boolean createData() {
        Log.d(TAG, "createData: ");

        //set time sleep for threads[
        int delayTime = mTimeBar.getProgress();
        for (AlgorithmThread thread : threads) {
            thread.setDelayTime(delayTime);
        }

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
            for (SortAlgorithmThread thread : threads) {
                thread.setData(array.clone());
            }
            return true;
        } else {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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
    protected void pauseSort() {
        for (AlgorithmThread thread : threads) {
            thread.setPaused(true);
            mPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            mPlayAnimation.hide();
        }
    }

    @Override
    protected void resumeSort() {
        for (AlgorithmThread thread : threads) {
            thread.setPaused(false);
        }
        mPlay.setImageResource(R.drawable.ic_pause_white_24dp);
        mPlayAnimation.show();
    }

    @Override
    public void onSortCompleted() {
        numThreadCompleted++;
        if (numThreadCompleted == threads.length) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.sorted,
                                Toast.LENGTH_SHORT).show();
                        enableView();
                        mPlayAnimation.hide();
                        mPlay.setImageResource(R.drawable.ic_settings_backup_restore_white_24dp);
                    }
                });
            }
        }
        super.onSortCompleted();

    }
}

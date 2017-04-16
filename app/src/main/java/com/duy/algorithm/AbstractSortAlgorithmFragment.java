package com.duy.algorithm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import static com.duy.algorithm.algorithms.AlgorithmThread.KEY_ALGORITHM;

/**
 * Created by DUy on 04-Feb-17.
 */

public abstract class AbstractSortAlgorithmFragment extends Fragment implements SortCompletionListener {
    final String TAG = AbstractSortAlgorithmFragment.class.getSimpleName();
    protected View mRootView;
    protected FloatingActionButton mPlay;
    protected FABProgressCircle mPlayAnimation;
    protected TextView txtInfo;
    protected EditText mEditNumber;
    protected SeekBar mTimeBar;
    protected TextView txtProgress;
    protected Button btnRandom;
    protected EditText mEditArray;
    protected SlidingUpPanelLayout mSlidingUpPanelLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = getView(inflater, container);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createSortView();

        mPlay = (FloatingActionButton) findViewById(R.id.fab);
        mPlayAnimation = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        txtProgress = (TextView) findViewById(R.id.txt_process);
        mTimeBar = (SeekBar) findViewById(R.id.seek_bar_time);

        /**
         * length of array and array
         */
        mEditNumber = (EditText) findViewById(R.id.edit_length);
        mEditArray = (EditText) findViewById(R.id.edit_entry);

        btnRandom = (Button) findViewById(R.id.ckb_random);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        //set up fragment
        setupFragment(getArguments().getString(KEY_ALGORITHM));

        txtProgress.setText(" " + (mTimeBar.getProgress() + 10));
        //set time delay for thread
        mTimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                txtProgress.setText(" " + (value + 10));
                setDelayTime(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomData();
//                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        mPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick();
            }
        });

    }

    protected abstract void fabClick();

    protected abstract void createSortView();

    /**
     * start thread sort
     */
    protected abstract void startSort();

    /**
     * pause all thread sort
     */
    protected abstract void pauseSort();

    /**
     * resume all thread
     */
    protected abstract void resumeSort();

    protected abstract void setDelayTime(int value);

    /**
     * create random data
     */
    protected abstract void generateRandomData();

    public abstract void setupFragment(String name);

    /**
     * find view by id and return view, if view is null, find view in parent activity
     *
     * @param viewId - id of view
     * @return view
     */
    public View findViewById(int viewId) {
        View view;
        view = mRootView.findViewById(viewId);
        if (view != null) return view;
        return getActivity().findViewById(viewId);
    }

    /**
     * get container view and store it to mRootView
     *
     * @return - container view
     */
    public abstract View getView(LayoutInflater inflater, ViewGroup v);

    /**
     * check input empty and create data if need
     *
     * @return - <tt>true</tt> if success, otherwise <tt>false</tt>
     */
    public abstract boolean createData();

    /**
     * enable some view of needed
     */
    public void enableView() {
        Log.d(TAG, "enableView: ");
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEditArray.setEnabled(true);
                    mEditNumber.setEnabled(true);
                    btnRandom.setEnabled(true);
                }
            });
        }
    }

    /**
     * disable some view if needed
     */
    public void disableView() {
        Log.d(TAG, "disableView: ");
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEditArray.setEnabled(false);
                    mEditNumber.setEnabled(false);
                    btnRandom.setEnabled(false);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        resumeSort();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        try {
            pauseSort();            enableView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSortCompleted() {

    }

}

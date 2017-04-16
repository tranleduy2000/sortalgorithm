package com.duy.algorithm.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.algorithm.R;

import java.util.ArrayList;

/**
 * Created by DUy on 04-Feb-17.
 */

public class LogView extends RecyclerView {
    private LogAdapter mLogAdapter;
    private View mEmptyView;

    public LogView(Context context) {
        super(context);
        setup(context);
    }

    public LogView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public LogView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);

    }

    private void setup(Context context) {
        if (!isInEditMode()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setStackFromEnd(true);
            setLayoutManager(linearLayoutManager);
            mLogAdapter = new LogView.LogAdapter(context);
            setAdapter(mLogAdapter);
        }
    }

    public void addLog(String log) {
        mLogAdapter.addLog(log);
        if (mEmptyView != null) mEmptyView.setVisibility(GONE);
        scrollToPosition(mLogAdapter.getItemCount() - 1);
    }

    public void addLog(String log, int[] array) {
        if (mEmptyView != null) mEmptyView.setVisibility(GONE);

        String str = "[";
        for (int i = 0; i < array.length - 1; i++) {
            str += array[i];
            str += ", ";
        }
        if (array.length > 0) {
            str += array[array.length - 1];
            str += "]";
        }
        mLogAdapter.addLog(log + " " + str);
        scrollToPosition(mLogAdapter.getItemCount() - 1);

    }

    public void setmEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }

    public static class LogAdapter extends Adapter<LogAdapter.LogHolder> {
        private ArrayList<String> mLogs = new ArrayList<>();
        private Context context;

        public LogAdapter(Context context) {
            this.context = context;
        }


        @Override
        public LogHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_item, viewGroup, false);
            return new LogHolder(v);
        }

        @Override
        public void onBindViewHolder(LogHolder itemHolder, int i) {
            itemHolder.txtLog.setText(mLogs.get(i));
        }

        @Override
        public int getItemCount() {
            return (null != mLogs ? mLogs.size() : 0);
        }

        public void addLog(String log) {
            mLogs.add(log);
            notifyItemInserted(getItemCount() - 1);
        }

        public void clearLog() {
            notifyItemRangeRemoved(0, getItemCount());
            mLogs.clear();
        }

        public static class LogHolder extends ViewHolder {

            TextView txtLog;

            public LogHolder(View view) {
                super(view);
                txtLog = (TextView) view.findViewById(R.id.txt_log);
            }

        }

    }
}

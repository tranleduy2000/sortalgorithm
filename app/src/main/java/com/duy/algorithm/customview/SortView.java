package com.duy.algorithm.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.duy.algorithm.R;

/**
 * create by Mr.Duy on 02-Feb-17
 */
public class SortView extends View {

    public static final String TAG = "SortView";
    private static final String NO_DATA = "No Data!";
    private float[][] tmp = new float[2][2];    //save coordinate of two index bar
    private int[] array;    //input data
    private Paint mPaint;
    private boolean isDrawing = false;
    private String name = "";
    private Context context;
    private long mTime = 0;
    private int swapAPosition = -1;
    private int swapBPosition = -1;
    private int tracePosition = -1;
    private int targetPosition = -1;
    private int completePosition = -1;
    private int barColor = Color.WHITE;
    private int targetColor = Color.GREEN;
    private int swapAColor = Color.RED;
    private int swapBColor = Color.MAGENTA;
    private int traceColor = Color.BLUE;
    private int quadColor = Color.GREEN;
    private int completeColor = Color.GREEN;
    private int textInfoColor = Color.RED;
    private Handler handler = new Handler();
    public float xA = 0;
    public float xB = 0;
    private float yA = 0;
    private float yB = 0;
    private int delta = 0;

    public SortView(Context context) {
        super(context);
        setup(context, null, -1);
    }

    public SortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, -1);
    }

    public SortView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setup(context, attrs, defStyleAttr);

    }


    public int getCompletePosition() {
        return completePosition;
    }

    public void setCompletePosition(int completePosition) {
        this.completePosition = completePosition;
    }

    /**
     * find max value in array
     *
     * @param arr - input array
     * @return - max value
     */
    private int getMax(int[] arr) {
        int N = arr.length;
        int max = arr[0];
        for (int i = 1; i < N; i++) {
            if (max < arr[i]) max = arr[i];
        }
        return max;
    }

    public void setup(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SortViewAttrs);

        barColor = a.getInteger(R.styleable.SortViewAttrs_bar_color, barColor);
        targetColor = a.getInteger(R.styleable.SortViewAttrs_target_color, targetColor);
        traceColor = a.getInteger(R.styleable.SortViewAttrs_trace_color, traceColor);
        quadColor = a.getInteger(R.styleable.SortViewAttrs_quad_color, quadColor);
        completeColor = a.getInteger(R.styleable.SortViewAttrs_complete_color, completeColor);
        textInfoColor = a.getInteger(R.styleable.SortViewAttrs_text_info_color, textInfoColor);
        swapAColor = a.getInteger(R.styleable.SortViewAttrs_swap_a_color, swapAColor);
        swapBColor = a.getInteger(R.styleable.SortViewAttrs_swap_b_color, swapBColor);
        a.recycle();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(barColor);
        mPaint.setAntiAlias(true);
        setTextSize(20f);
        array = null;
    }

    /**
     * calculate and set text size for mPaint
     */
    private void setTextSize(float GESTURE_THRESHOLD_DIP) {
        // The gesture threshold expressed in dip
        // Convert the dips to pixels
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int mGestureThreshold = (int) (GESTURE_THRESHOLD_DIP * scale + 0.5f);

        mPaint.setTextSize(mGestureThreshold);
    }

    /**
     * draw data from array @array
     *
     * @param canvas - canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //if data is available
        if (array != null && array.length > 0) {
            drawArray(canvas);
        } else {
            drawNoData(canvas);
        }
        drawInfo(canvas);
    }

    /**
     * draw text <tt>name</tt> and <tt>mTime</tt> in top-left of view
     */
    private void drawInfo(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1.0f);
        mPaint.setColor(textInfoColor);
        setTextSize(10f);

        Rect rect = new Rect();
        mPaint.getTextBounds("A", 0, 1, rect);

        int x = rect.width();
        int y = rect.height() + rect.height() / 2;

        canvas.drawText(name, x, y, mPaint);

        y += (rect.height() + rect.height() / 2);
        canvas.drawText("Complexity: " + mTime, x, y, mPaint);
    }

    /**
     * draw array data
     *
     * @param canvas
     */
    private void drawArray(Canvas canvas) {
        int width = getWidth(); //get width of view
        int height = getHeight(); //get height of view
        //number entry of array
        int N = array.length;

        //calculate width of bar
        float barWidth = width / (N + 1);
        mPaint.setStrokeWidth(barWidth * 0.8f);

        int max = getMax(array);

        //calculate dip per bar entry
        float per = height / (max + 1);

        //start x, start y, start y is height because axis of screen
        // top-left (0;0) -> right-bottom (width;height)
        float x = 0;
        float y = height;

        //start index
        int index = 0;

        //foreach
        for (int a : array) {
            x += barWidth;//coordinate of next bar
            if (index <= completePosition) {
                //set color for bar entry
                mPaint.setColor(completeColor);
                canvas.drawLine(x, y, x, y - (a * per), mPaint);//draw bar
            } else {
                if (index == swapAPosition) { //swap0
//                    Log.d(TAG, "drawArray: " + swapAPosition);
                    mPaint.setColor(swapAColor);
                    //draw highlight bar
                    canvas.drawLine(xA, yA, xA, yA + (a * per), mPaint);

                } else if (index == swapBPosition) { //swap1
//                    Log.d(TAG, "drawArray: " + swapBPosition);
                    mPaint.setColor(swapBColor);
                    //draw highlight bar
                    canvas.drawLine(xB, yB, xB, yB + (a * per), mPaint);

                } else if (index == tracePosition) { //trace
                    mPaint.setColor(traceColor);
                    canvas.drawLine(x, y, x, y - (a * per), mPaint);//draw highlight bar
                } else {
                    //set color for bar entry
                    mPaint.setColor(barColor);
                    canvas.drawLine(x, y, x, y - (a * per), mPaint);//draw bar
                }
                if (index == targetPosition) { //target

                    //set color for bar entry
                    mPaint.setColor(targetColor);
                    canvas.drawLine(x, y, x, y - (a * per), mPaint);//draw highlight bar
                }
            }
            index++;
        }
        if (swapAPosition != swapBPosition) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(2.0f);
            mPaint.setColor(quadColor);
            Path path = new Path();
            path.moveTo(xA, yA);

            path.quadTo(xA + Math.abs(xB - xA) * 2 / 3, y - max * per, xB, yB);
            canvas.drawPath(path, mPaint);
        }
    }

    private void drawNoData(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1.0f);
        mPaint.setColor(textInfoColor);
        canvas.drawText(NO_DATA, getWidth() / 2, getHeight() / 2, mPaint);
    }

    public void setTracePosition(int index) {
        this.tracePosition = index;
        invalidate();
    }

    public void setArray(int[] arr) {
        this.array = arr;
    }

    @UiThread
    public void setSwapPosition(int i1, int i2) {
        setSwapPosition(i1, i2, true);
    }

    @UiThread
    public void setSwapPosition(int i1, int i2, boolean redraw) {
        if (i1 < 0 || i2 < 0) {
            swapAPosition = i1;
            swapBPosition = i2;
            if (redraw) invalidate();
            return;
        }
        if (i1 < i2) {
            swapAPosition = i1;
            swapBPosition = i2;
        } else {
            swapAPosition = i2;
            swapBPosition = i1;
        }

        int width = getWidth(); //get width of view
        int height = getHeight(); //get height of view
        int N = array.length;
        float barWidth = width / (N + 1);
        int max = getMax(array);
        //calculate pixel per bar entry
        float per = height / (max + 1);

        xA = barWidth * (swapAPosition + 1);
        yA = height - (array[swapAPosition] * per);

        xB = barWidth * (swapBPosition + 1);
        yB = height - (array[swapBPosition] * per);
        delta = (int) Math.abs(xB - xA);
        if (redraw) invalidate();
    }


    public int getSizeArray() {
        return array.length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(int id) {
        this.name = context.getString(id);
    }

    @UiThread
    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
        invalidate();
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public void addTimeUnit(long time) {
        this.mTime += time;
    }

    /**
     * change value of xA, xB
     */
    @UiThread
    public void incPositionSwap(float v) {
        xA += v;
        xB -= v;
        invalidate();
    }

    public int getDelta() {
        return delta;
    }
}

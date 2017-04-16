package com.duy.algorithm.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;

/**
 * Created by DUy on 03-Feb-17.
 */

public class EditTextInputArray extends AppCompatEditText {

    public EditTextInputArray(Context context) {
        super(context);
        init(context);
    }

    public EditTextInputArray(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public EditTextInputArray(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return "1234567890,".toCharArray();
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
            }
        });
    }

}

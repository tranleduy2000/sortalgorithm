package com.duy.algorithm.userinterface;

import android.content.Context;
import android.content.res.Resources;

import com.duy.algorithm.R;


/**
 * Created by Duy on 3/7/2016
 */
public class ThemeEngine {
    public static final String NULL = "";
    public static final int THEME_NOT_FOUND = -1;
    private final int mLightTheme;
    private final int mDarkTheme;
    private Resources mResources;

    public ThemeEngine(Context applicationContext) {
        this.mResources = applicationContext.getResources();

        mLightTheme = R.style.AppTheme_Light_NoActionBar;
        mDarkTheme = R.style.AppTheme_Dark_NoActionBar;

    }

    /**
     * get theme from mResult
     *
     * @param name
     * @return
     */
    public int getTheme(String name) {
        name = name.trim();
        if (name.equals(NULL)) {
            return THEME_NOT_FOUND;
        }
        if (name.equals(mResources.getString(R.string.theme_light))) {
            return mLightTheme;
        } else if (name.equals(mResources.getString(R.string.theme_dark))) {
            return mDarkTheme;
        }
        return mLightTheme;
    }
}


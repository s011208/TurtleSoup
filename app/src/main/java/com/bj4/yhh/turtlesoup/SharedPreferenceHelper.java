package com.bj4.yhh.turtlesoup;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yenhsunhuang on 15/2/5.
 */
public class SharedPreferenceHelper {
    private static final String TAG = "SharedPreferenceHelper";
    private static final String HAS_LOADED_DB = "has_loaded_db";
    private static final String PREVIOUS_INDEX = "previous_inde";
    private static SharedPreferenceHelper sInstance;

    public synchronized static SharedPreferenceHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private SharedPreferences mPrefs;

    private SharedPreferenceHelper(Context mContext) {
        mPrefs = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public int getPreviousIndex() {
        return mPrefs.getInt(PREVIOUS_INDEX, 0);
    }

    public void setPreviousIndex(int index) {
        mPrefs.edit().putInt(PREVIOUS_INDEX, index).apply();
    }

    public boolean hasLoadedDb() {
        return mPrefs.getBoolean(HAS_LOADED_DB, false);
    }

    public void setLoaded() {
        mPrefs.edit().putBoolean(HAS_LOADED_DB, true).commit();
    }
}

package com.bj4.yhh.turtlesoup;

import android.app.Application;

/**
 * Created by yenhsunhuang on 15/2/6.
 */
public class TurtulSoupApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StoryDatabaseHelper.getInstnace(this);
    }
}

package com.bj4.yhh.turtlesoup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yenhsunhuang on 15/2/3.
 */
public class StoryList extends ThemeChangeFragment {

    @Override
    public void onThemeChanged(int theme) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.firstfragment, container, false);
        return v;
    }
}

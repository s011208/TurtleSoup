package com.bj4.yhh.turtlesoup;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class MainActivity extends Activity {
    public static final int FRAGMENT_STATUS_ENTRY = 0;
    public static final int FRAGMENT_STATUS_STORY_LIST = 1;
    private int mFragmentStatus = FRAGMENT_STATUS_ENTRY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new EntryFragment());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentStatus == FRAGMENT_STATUS_ENTRY) {
            super.onBackPressed();
        } else if (mFragmentStatus == FRAGMENT_STATUS_STORY_LIST) {
            switchToEntryFragment();
        }
    }

    public void switchToStoryFragment() {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out);
        transaction.replace(R.id.fragment_container, new StoryList());
        transaction.commit();
        mFragmentStatus = FRAGMENT_STATUS_STORY_LIST;
    }

    public void switchToEntryFragment() {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out);
        transaction.replace(R.id.fragment_container, new EntryFragment());
        transaction.commit();
        mFragmentStatus = FRAGMENT_STATUS_ENTRY;
    }
}

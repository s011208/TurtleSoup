package com.bj4.yhh.turtlesoup;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {
    private static final String PREVIOUS_FRAGMENT_STATUS = "previous_fragment_status";
    public static final int FRAGMENT_STATUS_ENTRY = 0;
    public static final int FRAGMENT_STATUS_STORY_LIST = 1;
    private int mFragmentStatus = FRAGMENT_STATUS_ENTRY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mFragmentStatus = savedInstanceState.getInt(PREVIOUS_FRAGMENT_STATUS, FRAGMENT_STATUS_ENTRY);
        }
        if (mFragmentStatus == FRAGMENT_STATUS_ENTRY) {
            switchToEntryFragment();
        } else if (FRAGMENT_STATUS_ENTRY == FRAGMENT_STATUS_STORY_LIST) {
            switchToStoryFragment();
        }
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new EntryFragment());
        transaction.commitAllowingStateLoss();
//        startParseTurtleSoupTask(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putInt(PREVIOUS_FRAGMENT_STATUS, mFragmentStatus);
    }

    public static void startParseTurtleSoupTask(Context context) {
        Intent parse = new Intent(context, ParseStoryService.class);
        parse.setAction(ParseStoryService.INTENT_START_PARSE);
        parse.putExtra(ParseStoryService.INTENT_EXTRAS_TURTLESOUP, "");
        context.startService(parse);
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

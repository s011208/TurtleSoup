package com.bj4.yhh.turtlesoup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;

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
        final View parent = inflater.inflate(R.layout.story_list_fragmnet, null);
        final ViewPager storyPager = (ViewPager) parent.findViewById(R.id.story_pager);
        storyPager.setAdapter(new StoryPagerAdapter(getActivity(), 10));
        return parent;
    }

    private static class StoryPagerAdapter extends PagerAdapter {
        private final WeakReference<Context> mContext;
        private final WeakReference<LayoutInflater> mInflater;
        private final int mItemsInPage;
        private final int mTotalItems = 100;

        public StoryPagerAdapter(Context context, final int itemsInPage) {
            mContext = new WeakReference<Context>(context);
            mInflater = new WeakReference<LayoutInflater>((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            mItemsInPage = itemsInPage;
        }

        @Override
        public int getCount() {
            return itemsInPage > 0 ? mTotalItems / itemsInPage : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == (View) obj;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            View itemView = initView(position);
            ((ViewPager) collection).addView(itemView, 0);
            return itemView;
        }

        private View initView(final int position) {
            final LayoutInflater inflater = mInflater.get();
            if (inflater == null) {
                throw new RuntimeException("lost Layout Inflater");
            }
            ListView storyList = (ListView) inflater.inflate(R.layout.story_pager_content, null);
            if (storyList == null) {
                throw new RuntimeException("wrong story pager content");
            }

            return storyList;
        }

        private class StoryListAdapter extends BaseAdapter {
            private final Context mContext;
            private final LayoutInflater mInflater;

            public StoryListAdapter(Context context, LayoutInflater inflater) {
                mContext = context;
                mInflater = inflater;
            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        }
    }
}

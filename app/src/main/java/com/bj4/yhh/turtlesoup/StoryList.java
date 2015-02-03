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
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
        final int itemPerPage = getActivity().getResources().getInteger(R.integer.items_paer_page);
        storyPager.setAdapter(new StoryPagerAdapter(getActivity(), itemPerPage));
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
            return mItemsInPage > 0 ? mTotalItems / mItemsInPage : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == (View) obj;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            View itemView = initView(position);
            ((ViewPager) collection).addView(itemView, 0);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private View initView(final int position) {
            final LayoutInflater inflater = mInflater.get();
            final Context context = mContext.get();
            if (inflater == null || context == null) {
                throw new RuntimeException("lost Layout Inflater");
            }
            ListView storyList = (ListView) inflater.inflate(R.layout.story_pager_content, null);
            if (storyList == null) {
                throw new RuntimeException("wrong story pager content");
            }
            storyList.setAdapter(new StoryListAdapter(context, inflater, position * mItemsInPage, (position + 1) * mItemsInPage));
            return storyList;
        }

        private class StoryListAdapter extends BaseAdapter {
            private final Context mContext;
            private final LayoutInflater mInflater;
            private final int mStartIndex;
            private final int mOffset;

            private final ArrayList<Story> mStories = new ArrayList<Story>();

            public StoryListAdapter(Context context, LayoutInflater inflater, final int startIndex, final int offset) {
                mContext = context;
                mInflater = inflater;
                mStartIndex = startIndex;
                mOffset = offset;
                initStories();
            }

            private void initStories() {
                for (int i = 0; i < mOffset - mStartIndex; i++) {
                    mStories.add(new Story("a", "b", "c", "d"));
                }
            }

            @Override
            public int getCount() {
                return mStories.size();
            }

            @Override
            public Story getItem(int position) {
                return mStories.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.story_list_content, null);
                    holder = new ViewHolder();
                    holder.mTitle = (TextView) convertView.findViewById(R.id.title);
                    holder.mSummary = (TextView) convertView.findViewById(R.id.summary);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.mTitle.setText(getItem(position).getTitle());
                holder.mSummary.setText(getItem(position).getSummary());
                return convertView;
            }

            private class ViewHolder {
                TextView mTitle, mSummary;
            }
        }
    }
}

package com.bj4.yhh.turtlesoup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by yenhsunhuang on 15/2/3.
 */
public class StoryList extends ThemeChangeFragment {

    public static final String REQUEST_TO_UPDATE_STORIES = "request_to_update_stories";

    private ViewPager mStoryPager;

    private int mCurrentIndex;

    private int mItemPerPage = 10;

    public static final ArrayList<Story> sStories = new ArrayList<Story>();

    @Override
    public void onThemeChanged(int theme) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reloadStoriesAsync();
    }

    private void reloadStoriesAsync() {
        new AsyncTask<Void, Void, Void>() {
            private final ArrayList<Story> mStories = new ArrayList<Story>();

            @Override
            protected Void doInBackground(Void... params) {
                mStories.addAll(StoryDatabaseHelper.getInstnace(getActivity()).queryStories());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                sStories.clear();
                sStories.addAll(mStories);
                if (mStoryPager != null) {
                    mStoryPager.setAdapter(new StoryPagerAdapter(getActivity(), mItemPerPage));
                    mStoryPager.setCurrentItem(mCurrentIndex);
                }
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mItemPerPage = getActivity().getResources().getInteger(R.integer.items_paer_page);
        mCurrentIndex = SharedPreferenceHelper.getInstance(getActivity()).getPreviousIndex();
        final View parent = inflater.inflate(R.layout.story_list_fragmnet, null);
        mStoryPager = (ViewPager) parent.findViewById(R.id.story_pager);
        if (sStories.isEmpty() == false) {
            mStoryPager.setAdapter(new StoryPagerAdapter(getActivity(), mItemPerPage));
            mStoryPager.setCurrentItem(mCurrentIndex);
            mStoryPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {

                }

                @Override
                public void onPageSelected(int i) {
                    mCurrentIndex = i;
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
        return parent;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            reloadStoriesAsync();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REQUEST_TO_UPDATE_STORIES);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferenceHelper.getInstance(getActivity()).setPreviousIndex(mStoryPager.getCurrentItem());
        getActivity().unregisterReceiver(mReceiver);
    }

    private static class StoryPagerAdapter extends PagerAdapter {
        private final WeakReference<Context> mContext;
        private final WeakReference<Activity> mActivity;
        private final WeakReference<LayoutInflater> mInflater;
        private final int mItemsInPage;
        private final int mTotalItems;
        private Handler mHandler = new Handler();

        public StoryPagerAdapter(Activity activity, final int itemsInPage) {
            mActivity = new WeakReference<Activity>(activity);
            mContext = new WeakReference<Context>(activity);
            mInflater = new WeakReference<LayoutInflater>((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            mItemsInPage = itemsInPage;
            mTotalItems = StoryDatabaseHelper.getInstnace(activity).getStoryCount();
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
            final StoryListAdapter adapter = new StoryListAdapter(context, inflater, position * mItemsInPage, (position + 1) * mItemsInPage);
            storyList.setAdapter(adapter);
            storyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Story story = adapter.getItem(position);
                    StoryContentDialog dialog = StoryContentDialog.getNewInstance(mContext.get(), story);
                    dialog.show(mActivity.get().getFragmentManager(), StoryContentDialog.TAG);
                    story.setRead(true);
                    StoryDatabaseHelper.getInstnace(mActivity.get()).setRead(story);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, 30);
                }
            });
            return storyList;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
                mStories.addAll(sStories.subList(mStartIndex, mOffset >= sStories.size() ? sStories.size() - 1 : mOffset));
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
                    holder.mHint = (ImageView) convertView.findViewById(R.id.new_hint);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final Story story = getItem(position);
                holder.mTitle.setText(story.getTitle());
                holder.mSummary.setText(story.getSummary());
                holder.mHint.setVisibility(story.hasRead() ? View.INVISIBLE : View.VISIBLE);
                return convertView;
            }

            private class ViewHolder {
                TextView mTitle, mSummary;
                ImageView mHint;
            }
        }
    }
}

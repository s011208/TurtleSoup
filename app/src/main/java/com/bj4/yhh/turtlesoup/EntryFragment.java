package com.bj4.yhh.turtlesoup;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by yenhsunhuang on 15/2/3.
 */
public class EntryFragment extends ThemeChangeFragment {
    @Override
    public void onThemeChanged(int theme) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ListView parent = (ListView) inflater.inflate(R.layout.entry_fragment, null);
        parent.setAdapter(new EntryListAdapter(getActivity()));
        parent.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        ((MainActivity)getActivity()).switchToStoryFragment();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        return parent;
    }

    private static class EntryListAdapter extends BaseAdapter {
        private final WeakReference<Context> mContext;
        private final WeakReference<LayoutInflater> mInflater;
        private final ArrayList<String> mItems = new ArrayList<String>();

        public EntryListAdapter(Context context) {
            mContext = new WeakReference<Context>(context);
            mInflater = new WeakReference<LayoutInflater>((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            initItem();
        }

        private void initItem() {
            mItems.clear();
            final Resources res = mContext.get().getResources();
            mItems.add(res.getString(R.string.turtle_soup));
            mItems.add(res.getString(R.string.instructions));
            mItems.add(res.getString(R.string.settings));
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public String getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.get().inflate(R.layout.entry_fragment_content, null);
                holder = new ViewHolder();
                holder.mItem = (TextView) convertView.findViewById(R.id.item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mItem.setText(getItem(position));
            return convertView;
        }

        private class ViewHolder {
            TextView mItem;
        }
    }
}

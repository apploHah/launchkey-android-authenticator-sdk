package com.launchkey.android.authenticator.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.launchkey.android.authenticator.demo.R;

/**
 * Created by armando on 7/8/16.
 */
public class DemoFeatureAdapter extends BaseAdapter {

    private Context mContext;
    private int[] mItems = new int[0];

    public DemoFeatureAdapter(Context context, int[] i) {
        mContext = context;
        mItems = i;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Integer getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) convertView;

        if (v == null) {
            v = (TextView) LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.demo_activity_list_item, parent, false);
        }

        v.setText(mContext.getString(getItem(position)));

        return v;
    }
}

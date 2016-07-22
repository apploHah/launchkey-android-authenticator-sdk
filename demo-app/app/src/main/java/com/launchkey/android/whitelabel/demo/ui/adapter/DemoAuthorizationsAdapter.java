package com.launchkey.android.whitelabel.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.sdk.auth.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by armando on 7/11/16.
 */
public class DemoAuthorizationsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Application> mAuthorizations = new ArrayList<>();
    private AdapterView.OnItemClickListener mItemClickListener;
    private View.OnClickListener mInternalClickListener;

    public DemoAuthorizationsAdapter(Context c, List<Application> authorizations, AdapterView.OnItemClickListener l) {
        mContext = c;
        mAuthorizations = authorizations;
        mItemClickListener = l;

        mInternalClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v != null && v.getTag() != null) {
                    int position = (int) v.getTag();

                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(null, v, position, position);
                    }
                }
            }
        };
    }

    @Override
    public int getCount() {
        return mAuthorizations.size();
    }

    @Override
    public Application getItem(int position) {
        return mAuthorizations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.demo_authorizations_item, parent, false);
        }

        Application a = getItem(position);

        //fetch an application's icon via its url with Application.getAppIcon()

        TextView name = (TextView) v.findViewById(R.id.demo_authorizations_item_name);
        name.setText(a.getName());

        TextView context = (TextView) v.findViewById(R.id.demo_authorizations_item_context);
        context.setText(a.getDetailsContext());

        TextView action = (TextView) v.findViewById(R.id.demo_authorizations_item_text_action);
        action.setText(a.getAction().toUpperCase());

        TextView status = (TextView) v.findViewById(R.id.demo_authorizations_item_text_status);
        status.setText(a.getStatus().toUpperCase());

        TextView transactional = (TextView) v.findViewById(R.id.demo_authorizations_item_text_transactional);
        transactional.setText(a.isTransactional() ? "TRANSACTIONAL" : "");

        Button button = (Button) v.findViewById(R.id.demo_authorizations_item_button);
        button.setText("LOG OUT");
        button.setOnClickListener(mInternalClickListener);
        button.setTag(position);

        return v;
    }
}

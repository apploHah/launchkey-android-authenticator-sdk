package com.launchkey.android.authenticator.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.launchkey.android.authenticator.sdk.session.Session;
import com.launchkey.android.authenticator.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by armando on 7/11/16.
 */
public class DemoSessionsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Session> mSessions = new ArrayList<>();
    private AdapterView.OnItemClickListener mItemClickListener;
    private View.OnClickListener mInternalClickListener;

    public DemoSessionsAdapter(Context c, List<Session> sessions, AdapterView.OnItemClickListener l) {
        mContext = c;
        mSessions = sessions;
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
        return mSessions.size();
    }

    @Override
    public Session getItem(int position) {
        return mSessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Relying on the authorizations layout for items

        View v = convertView;

        if (v == null) {
            v = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.demo_authorizations_item, parent, false);
        }

        Session s = getItem(position);

        //fetch an application's icon via its url with Application.getAppIcon()

        TextView name = (TextView) v.findViewById(R.id.demo_authorizations_item_name);
        name.setText(s.getName());

        //TODO: Update usage of ID as placeholder for potential context being sent
        TextView context = (TextView) v.findViewById(R.id.demo_authorizations_item_context);
        context.setText(s.getId());

        long millisAgo = s.getCreatedAgoMillis(mContext);

        TextView action = (TextView) v.findViewById(R.id.demo_authorizations_item_text_action);
        action.setText(String.format(Locale.getDefault(), "%d seconds ago", (millisAgo / 1000)));

        /*
        TextView status = (TextView) v.findViewById(R.id.demo_authorizations_item_text_status);
        status.setText(a.getStatus().toUpperCase());

        TextView transactional = (TextView) v.findViewById(R.id.demo_authorizations_item_text_transactional);
        transactional.setText(a.isTransactional() ? "TRANSACTIONAL" : "");
        */

        Button button = (Button) v.findViewById(R.id.demo_authorizations_item_button);
        button.setText("LOG OUT");
        button.setOnClickListener(mInternalClickListener);
        button.setTag(position);

        return v;
    }
}

package com.launchkey.android.whitelabel.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.sdk.logs.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by armando on 7/11/16.
 */
public class DemoLogsAdapter extends BaseAdapter {

    private Context mC;
    private List<Log> mL = new ArrayList<>();

    public DemoLogsAdapter(Context context, List<Log> logs) {
        mC = context;
        mL = logs;
    }

    @Override
    public int getCount() {
        return mL.size();
    }

    @Override
    public Log getItem(int position) {
        return mL.get(position);
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
                    .from(mC)
                    .inflate(R.layout.demo_logs_item, parent, false);
        }

        Log l = getItem(position);

        TextView timestamp = (TextView) v.findViewById(R.id.demo_logs_item_text_timestamp);
        timestamp.setText(l.timeAgo);

        TextView name = (TextView) v.findViewById(R.id.demo_logs_item_name);
        name.setText(l.appName);

        TextView device = (TextView) v.findViewById(R.id.demo_logs_item_device);
        device.setText(String.format("From device '%s'", l.deviceName));

        TextView context = (TextView) v.findViewById(R.id.demo_logs_item_context);
        context.setText(l.context);

        TextView action = (TextView) v.findViewById(R.id.demo_logs_item_text_action);
        action.setText(getActionFromLog(l));

        TextView status = (TextView) v.findViewById(R.id.demo_logs_item_text_status);
        status.setText(getStatusFromLog(l));

        TextView transactional = (TextView) v.findViewById(R.id.demo_logs_item_text_transactional);
        transactional.setVisibility(l.isTransactional() ? View.VISIBLE : View.GONE);

        return v;
    }

    private String getActionFromLog(Log l) {
        return l.action == Log.ACTION_AUTH ? "AUTHENTICATED" : "REVOKED";
    }

    private String getStatusFromLog(Log l) {
        switch (l.status) {
            case Log.STATUS_DENIED:
                return "DENIED";
            case Log.STATUS_PENDING:
                return "PENDING";
            case Log.STATUS_SENT:
                return "SENT";
            case Log.STATUS_UNCONFIRMED:
                return "UNCONFIRMED";
        }

        return "GRANTED";
    }
}

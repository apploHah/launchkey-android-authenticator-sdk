package com.launchkey.android.whitelabel.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.sdk.otp.Otp;
import com.launchkey.android.whitelabel.sdk.otp.OtpCenter;

import java.util.List;

/**
 * Created by armando on 7/11/16.
 */
public class DemoTotpsAdapter extends BaseAdapter {

    private Context mC;
    private List<Otp> mO;

    public DemoTotpsAdapter(Context context, List<Otp> otps) {
        mC = context;
        mO = otps;
    }

    @Override
    public int getCount() {
        return mO.size();
    }

    @Override
    public Otp getItem(int position) {
        return mO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(mC).inflate(R.layout.demo_totp_item, parent, false);
        }

        Otp o = getItem(position);

        TextView user = (TextView) v.findViewById(R.id.demo_totps_item_user);
        user.setText(o.account);

        TextView service = (TextView) v.findViewById(R.id.demo_totps_item_service);
        service.setText(o.issuer);

        String passcode;

        try {
            passcode = OtpCenter.getTokenForOtp(o);
        } catch (Exception e) {
            passcode = "000000";
        }

        TextView token = (TextView) v.findViewById(R.id.demo_totps_item_token);
        token.setText(passcode);

        return v;
    }
}

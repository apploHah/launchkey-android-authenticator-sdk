package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.launchkey.android.authenticator.sdk.otp.Otp;
import com.launchkey.android.authenticator.sdk.otp.OtpCenter;
import com.launchkey.android.authenticator.sdk.otp.OtpList;
import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.ui.adapter.DemoTotpsAdapter;

import java.util.Calendar;

/**
 * Created by armando on 7/8/16.
 */
public class CustomTotpsFragment extends BaseDemoFragment implements OtpCenter.OtpLoaderListener {

    private ProgressBar mProgress;
    private ListView mList;
    private DemoTotpsAdapter mAdapter;
    private OtpCenter mOtpCenter;
    private OtpList mOtps;
    private Handler mUpdater;
    private Runnable mUpdate;
    private boolean mResumed = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.demo_fragment_totps, container, false);
        return postInflationSetup(root);
    }

    private View postInflationSetup(View root) {
        Button button = (Button) root.findViewById(R.id.demo_fragment_totps_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRandomTotp();
            }
        });

        mProgress = (ProgressBar) root.findViewById(R.id.demo_fragment_totps_progressbar);
        mList = (ListView) root.findViewById(R.id.demo_fragment_totps_list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                removeTotpAt(position);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOtpCenter = OtpCenter.getInstance(getActivity());
        mUpdater = new Handler();

        final long interval = 1000L/35L; //1000/XX, interval for XX fps

        mUpdate = new Runnable() {
            @Override
            public void run() {

                //get seconds and milli and offset to the first half of the minute

                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);

                if (seconds >= 30) {
                    seconds -= 30;
                }

                int progress = (int) ((float) seconds * 100f / 30f);

                if (mResumed) {
                    mProgress.setProgress(progress);
                    mUpdater.postDelayed(this, interval);
                }

                if (seconds < 5) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mResumed = true;
        mOtpCenter.getOtps(this);
        mUpdater.post(mUpdate);
    }

    @Override
    public void onPause() {
        super.onPause();
        mResumed = false;
        mOtpCenter.setOtps(mOtps);
    }

    @Override
    public void onOtpsLoaded(OtpList otpList) {
        mOtps = otpList;
        mAdapter = new DemoTotpsAdapter(getActivity(), mOtps.otps);
        mList.setAdapter(mAdapter);
    }

    private void removeTotpAt(int position) {
        mOtps.otps.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    private int mSecretsIndex = 0;
    private String[] mSecrets = new String[] {
            "andkdfgito3nnngu",
            "ryrhpp4zwzarwfhi",
            "e6sctovc7qg2nplf"
    };

    private void createRandomTotp() {
        mSecretsIndex = mSecretsIndex >= mSecrets.length - 1 ? 0 : mSecretsIndex + 1;

        Otp newOtp = new Otp();
        newOtp.account = "username";
        newOtp.issuer = "service";
        newOtp.secret = mSecrets[mSecretsIndex];;

        mOtps.otps.add(newOtp);
        mAdapter.notifyDataSetChanged();
    }
}

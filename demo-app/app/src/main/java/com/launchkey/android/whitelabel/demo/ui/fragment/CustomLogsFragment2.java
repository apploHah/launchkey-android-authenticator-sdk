package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.ui.adapter.DemoLogsAdapter;
import com.launchkey.android.whitelabel.demo.util.Utils;
import com.launchkey.android.whitelabel.sdk.error.BaseError;
import com.launchkey.android.whitelabel.sdk.logs.Log;
import com.launchkey.android.whitelabel.sdk.logs.LogCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by armando on 7/8/16.
 */
public class CustomLogsFragment2 extends BaseDemoFragment implements LogCenter.LogsListener2 {

    private List<Log> mLogs = new ArrayList<>();
    private ListView mList;
    private DemoLogsAdapter mAdapter;
    private LogCenter mLogCenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.demo_fragment_logs, container, false);
        return postInflationSetup(root);
    }

    private View postInflationSetup(View root) {
        mAdapter = new DemoLogsAdapter(getActivity(), mLogs);

        mList = (ListView) root.findViewById(R.id.demo_fragment_logs_list);
        mList.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLogCenter = LogCenter.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mLogCenter.addListener(this);
        mLogCenter.refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLogCenter.removeListener(this);
    }

    @Override
    public void onLogsUpdate(boolean success, List<Log> list, BaseError baseError) {
        if (success) {
            mLogs.clear();
            mLogs.addAll(list);
            mAdapter.notifyDataSetChanged();
        } else {
            Utils.simpleSnackbarForBaseError(mList, baseError);
        }
    }
}

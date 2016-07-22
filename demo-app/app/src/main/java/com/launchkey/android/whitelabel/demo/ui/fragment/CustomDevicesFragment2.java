package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.ui.adapter.DemoDevicesAdapter;
import com.launchkey.android.whitelabel.demo.util.Utils;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;
import com.launchkey.android.whitelabel.sdk.device.Device;
import com.launchkey.android.whitelabel.sdk.device.DeviceCenter;
import com.launchkey.android.whitelabel.sdk.error.BaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by armando on 7/8/16.
 */
public class CustomDevicesFragment2 extends BaseDemoFragment
        implements AdapterView.OnItemClickListener, DeviceCenter.DevicesListener2 {

    private List<Device> mDevices = new ArrayList<>();
    private ListView mList;
    private DemoDevicesAdapter mAdapter;
    private DeviceCenter mDeviceCenter;
    private boolean mUnlinkingCurrentDevice = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.demo_fragment_devices, container, false);
        return postInflationSetup(root);
    }

    private View postInflationSetup(View root) {
        mAdapter = new DemoDevicesAdapter(getActivity(), mDevices, this);

        mList = (ListView) root.findViewById(R.id.demo_fragment_devices_list);
        mList.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDeviceCenter = DeviceCenter.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mDeviceCenter.addListener(this);
        mDeviceCenter.refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDeviceCenter.removeListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mUnlinkingCurrentDevice = position == 0;

        if (mUnlinkingCurrentDevice) {
            getWhiteLabelManager().unlink(new WhiteLabelManager.UnlinkListener() {

                @Override
                public void onUnlink(boolean success, BaseError error) {
                    Utils.finish(CustomDevicesFragment2.this);
                }
            });
        } else {
            Device deviceToUnlink = mAdapter.getItem(position);
            mDeviceCenter.unlink(deviceToUnlink);
        }
    }

    @Override
    public void onDevicesUpdate(boolean success, List<Device> list, BaseError error) {
        if (success) {
            mDevices.clear();
            mDevices.addAll(list);
            mAdapter.notifyDataSetChanged();
        } else {
            Utils.simpleSnackbarForBaseError(mList, error);
        }
    }

    @Override
    public void onDeviceUnlink(boolean wasUnlinked, BaseError error) {
        if (wasUnlinked) {
            Utils.simpleSnackbar(mList, "Device unlinked", Snackbar.LENGTH_LONG);
            Utils.finish(this);
        } else {
            Utils.simpleSnackbarForBaseError(mList, error);
        }
    }
}

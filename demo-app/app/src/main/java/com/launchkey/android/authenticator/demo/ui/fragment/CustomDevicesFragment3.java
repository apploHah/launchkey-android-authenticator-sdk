package com.launchkey.android.authenticator.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.launchkey.android.authenticator.sdk.SimpleOperationCallback;
import com.launchkey.android.authenticator.sdk.device.Device;
import com.launchkey.android.authenticator.sdk.device.DeviceManager;
import com.launchkey.android.authenticator.sdk.device.event.GetDevicesEventCallback;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.demo.ui.adapter.DemoDevicesAdapter;
import com.launchkey.android.authenticator.demo.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by armando on 7/8/16.
 */
public class CustomDevicesFragment3 extends BaseDemoFragment
        implements AdapterView.OnItemClickListener {

    private List<Device> mDevices = new ArrayList<>();
    private ListView mList;
    private DemoDevicesAdapter mAdapter;
    private DeviceManager mDeviceManager;
    private GetDevicesEventCallback mGetDevicesCallback;

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

        mDeviceManager = DeviceManager.getInstance(getActivity());
        mGetDevicesCallback = new GetDevicesEventCallback() {

            @Override
            public void onEventResult(boolean successful, BaseError error, List<Device> devices) {

                if (successful) {
                    mDevices.clear();
                    mDevices.addAll(devices);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Utils.simpleSnackbarForBaseError(mList, error);
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mDeviceManager.registerForEvents(mGetDevicesCallback);
        mDeviceManager.getDevices();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDeviceManager.unregisterForEvents(mGetDevicesCallback);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Device deviceToUnlink = mAdapter.getItem(position);
        mDeviceManager.unlinkDevice(deviceToUnlink, new SimpleOperationCallback<Device>() {

            @Override
            public void onResult(boolean successful, BaseError error, Device device) {

                if (successful) {
                    String message = String.format(Locale.getDefault(), "Device \"%s\" unlinked", device.getName());
                    Snackbar.make(mList, message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Return", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.finish(CustomDevicesFragment3.this);
                                }
                            })
                            .show();
                    mDeviceManager.getDevices();
                } else {
                    Utils.simpleSnackbarForBaseError(mList, error);
                }
            }
        });
    }
}

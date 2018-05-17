package com.launchkey.android.authenticator.demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.launchkey.android.authenticator.demo.util.Utils;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.sdk.DeviceLinkedEventCallback;
import com.launchkey.android.authenticator.sdk.DeviceUnlinkedEventCallback;
import com.launchkey.android.authenticator.sdk.SimpleOperationCallback;
import com.launchkey.android.authenticator.sdk.device.Device;
import com.launchkey.android.authenticator.sdk.device.DeviceManager;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.authenticator.sdk.ui.fragment.DevicesFragment;
import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.demo.app.DemoApplication;
import com.launchkey.android.authenticator.demo.ui.adapter.DemoFeatureAdapter;
import com.launchkey.android.authenticator.demo.ui.fragment.CustomDevicesFragment3;
import com.launchkey.android.authenticator.demo.ui.fragment.CustomLinkingFragment;
import com.launchkey.android.authenticator.demo.ui.fragment.CustomLocalAuthRequestFragment;
import com.launchkey.android.authenticator.demo.ui.fragment.CustomLogoutFragment2;
import com.launchkey.android.authenticator.demo.ui.fragment.CustomSessionsFragment;
import com.launchkey.android.authenticator.demo.ui.fragment.CustomUnlinkFragment2;
import com.launchkey.android.authenticator.demo.ui.fragment.SecurityInfoFragment;

import java.util.Locale;

/**
 * Created by armando on 7/8/16.
 */
public class ListDemoActivity extends BaseDemoActivity implements AdapterView.OnItemClickListener {

    public static final String EXTRA_SHOW_REQUEST = "extraShowRequest";

    private static final String ERROR_DEVICE_UNLINKED = "Device is unlinked";
    private static final String ERROR_DEVICE_LINKED = "Device is already linked";

    private static final int[] FEATURES_RES = new int[] {
            R.string.demo_activity_list_feature_link_default_manual,
            R.string.demo_activity_list_feature_link_default_scanner,
            R.string.demo_activity_list_feature_link_custom,
            R.string.demo_activity_list_feature_security,
            R.string.demo_activity_list_feature_security_custom,
            R.string.demo_activity_list_feature_securityinfo,
            R.string.demo_activity_list_feature_requests_xml,
            R.string.demo_activity_list_feature_requests_local_xml,
            R.string.demo_activity_list_feature_logout_custom2,
            R.string.demo_activity_list_feature_unlink_custom2,
            R.string.demo_activity_list_feature_sessions_custom,
            R.string.demo_activity_list_feature_devices_default,
            R.string.demo_activity_list_feature_devices_custom3,
            R.string.demo_activity_list_feature_sendmetrics
    };

    private ListView mList;
    private DemoFeatureAdapter mAdapter;
    private AuthenticatorManager mAuthenticatorManager;
    private DeviceLinkedEventCallback mDeviceLinkedCallback;
    private DeviceUnlinkedEventCallback mDeviceUnlinkedCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_list);

        mAuthenticatorManager = AuthenticatorManager.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mAdapter = new DemoFeatureAdapter(this, FEATURES_RES);

        mList = (ListView) findViewById(R.id.list_listview);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);

        mDeviceLinkedCallback = new DeviceLinkedEventCallback() {
            @Override
            public void onEventResult(boolean successful, BaseError error, Device device) {
                updateUi();
            }
        };

        mDeviceUnlinkedCallback = new DeviceUnlinkedEventCallback() {
            @Override
            public void onEventResult(boolean successful, BaseError error, Object o) {
                updateUi();
            }
        };

        processIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent i) {
        if (i == null || !i.hasExtra(EXTRA_SHOW_REQUEST)) {
            return;
        }

        if (i.getBooleanExtra(EXTRA_SHOW_REQUEST, false)) {
            onItemClick(null, null, getPositionOfFeature(R.string.demo_activity_list_feature_requests_xml), 0);
        }
    }

    private int getPositionOfFeature(int feature) {
        for (int i = 0; i < FEATURES_RES.length; i++) {
            if (feature == FEATURES_RES[i]) {
                return i;
            }
        }

        return -1;
    }

    private void updateUi() {

        boolean assumingNotVisible = getSupportActionBar() == null;

        if (assumingNotVisible) {
            return;
        }

        boolean nowLinked = mAuthenticatorManager.isDeviceLinked();

        Device device = DeviceManager.getInstance(this).getCurrentDevice();
        final String message = nowLinked ? String.format(Locale.getDefault(), "\"%s\"", device.getName()) : "Device Unlinked";

        getSupportActionBar().setTitle(getString(R.string.demo_activity_list_title_format, message));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUi();
        DemoApplication.cancelRequestNotification();
        mAuthenticatorManager.registerForEvents(mDeviceLinkedCallback, mDeviceUnlinkedCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthenticatorManager.unregisterForEvents(mDeviceLinkedCallback, mDeviceUnlinkedCallback);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int featureStringId = mAdapter.getItem(position);

        boolean linked = mAuthenticatorManager.isDeviceLinked();

        Class fragmentClassName = null;

        switch (featureStringId) {
            case R.string.demo_activity_list_feature_link_default_manual:
                mAuthenticatorManager.startLinkingActivity(this, AuthenticatorManager.LINKING_METHOD_MANUAL);
                break;

            case R.string.demo_activity_list_feature_link_default_scanner:
                if (linked) {
                    showError(ERROR_DEVICE_LINKED);
                } else {
                    mAuthenticatorManager.startLinkingActivity(this, AuthenticatorManager.LINKING_METHOD_SCAN);
                }
                break;

            case R.string.demo_activity_list_feature_link_custom:
                if (linked) {
                    showError(ERROR_DEVICE_LINKED);
                } else {
                    fragmentClassName = CustomLinkingFragment.class;
            }
                break;

            case R.string.demo_activity_list_feature_security:
                mAuthenticatorManager.startSecurityActivity(this);
                break;

            case R.string.demo_activity_list_feature_security_custom:
                Intent customSecurityActivity = new Intent(this, CustomSecurityActivity.class);
                startActivity(customSecurityActivity);
                break;

            case R.string.demo_activity_list_feature_securityinfo:
                fragmentClassName = SecurityInfoFragment.class;
                break;

            case R.string.demo_activity_list_feature_requests_xml:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    Intent authRequestActivity = new Intent(this, AuthRequestActivity.class);
                    startActivity(authRequestActivity);
                }
                break;

            case R.string.demo_activity_list_feature_requests_local_xml:

                if (linked || DemoApplication.CONFIG_ALLOW_LAR) {
                    fragmentClassName = CustomLocalAuthRequestFragment.class;
                } else {
                    showError(ERROR_DEVICE_UNLINKED);
                }
                break;

            case R.string.demo_activity_list_feature_logout_custom2:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomLogoutFragment2.class;
                }
                break;

            case R.string.demo_activity_list_feature_unlink_custom2:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomUnlinkFragment2.class;
                }
                break;

            case R.string.demo_activity_list_feature_sessions_custom:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomSessionsFragment.class;
                }
                break;

            case R.string.demo_activity_list_feature_devices_default:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = DevicesFragment.class;
                }
                break;

            case R.string.demo_activity_list_feature_devices_custom3:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomDevicesFragment3.class;
                }
                break;

            case R.string.demo_activity_list_feature_sendmetrics:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {

                    SimpleOperationCallback<Void> metricsCallback = new SimpleOperationCallback<Void>() {

                        @Override
                        public void onResult(boolean ok, BaseError e, Void v) {

                            if (ListDemoActivity.this.isFinishing()) {
                                return;
                            }

                            String message = Utils.getMessageForMetrics(ok, e);

                            if (ok) {

                                Toast.makeText(ListDemoActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {

                                Utils.alert(ListDemoActivity.this, "Error sending metrics", message);
                            }
                        }
                    };

                    mAuthenticatorManager.sendMetrics(metricsCallback);
                }
                break;
        }

        if (fragmentClassName != null) {

            //The full class name of a Fragment will be passed to the activity
            // so it's automatically instantiated by name and placed in a container.
            Intent fragmentActivity = new Intent(this, GenericFragmentDemoActivity.class);
            fragmentActivity.putExtra(GenericFragmentDemoActivity.EXTRA_FRAGMENT_CLASS, fragmentClassName.getCanonicalName());
            fragmentActivity.putExtra(GenericFragmentDemoActivity.EXTRA_TITLE, getString(featureStringId));

            startActivity(fragmentActivity);
        }
    }

    private void showError(int messageRes) {
        showError(getString(messageRes));
    }

    private void showError(String message) {
        showMessage("Error: ".concat(message));
    }

    private void showMessage(int messageRes) {
        showMessage(getString(messageRes));
    }

    private void showMessage(String message) {
        Snackbar.make(mList, message, Snackbar.LENGTH_LONG).show();
    }
}

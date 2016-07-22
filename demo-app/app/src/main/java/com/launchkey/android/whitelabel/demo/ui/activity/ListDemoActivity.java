package com.launchkey.android.whitelabel.demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.ui.adapter.DemoFeatureAdapter;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomAuthorizationsFragment;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomAuthorizationsFragment2;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomDevicesFragment;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomDevicesFragment2;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomLinkingFragment;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomLogoutFragment;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomLogsFragment;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomLogsFragment2;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomTotpsFragment;
import com.launchkey.android.whitelabel.demo.ui.fragment.CustomUnlinkFragment;
import com.launchkey.android.whitelabel.demo.ui.fragment.SecurityInfoFragment;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;
import com.launchkey.android.whitelabel.sdk.ui.AuthRequestFragment;
import com.launchkey.android.whitelabel.sdk.ui.fragment.AuthorizationsFragment;
import com.launchkey.android.whitelabel.sdk.ui.fragment.DevicesFragment;
import com.launchkey.android.whitelabel.sdk.ui.fragment.LogsFragment;
import com.launchkey.android.whitelabel.sdk.ui.fragment.TotpsFragment;

/**
 * Created by armando on 7/8/16.
 */
public class ListDemoActivity extends BaseDemoActivity implements WhiteLabelManager.SessionListener, WhiteLabelManager.AccountStateListener, AdapterView.OnItemClickListener {

    private static final String ERROR_DEVICE_UNLINKED = "Device is unlinked";
    private static final String ERROR_DEVICE_LINKED = "Device is already linked";

    private static final int[] FEATURES_RES = new int[] {
            R.string.demo_activity_list_feature_link_default_manual,
            R.string.demo_activity_list_feature_link_default_scanner,
            R.string.demo_activity_list_feature_link_custom,
            R.string.demo_activity_list_feature_security,
            R.string.demo_activity_list_feature_securityinfo,
            R.string.demo_activity_list_feature_requests,
            R.string.demo_activity_list_feature_logout_default,
            R.string.demo_activity_list_feature_logout_custom,
            R.string.demo_activity_list_feature_unlink_default,
            R.string.demo_activity_list_feature_unlink_custom,
            R.string.demo_activity_list_feature_authorizations_default,
            R.string.demo_activity_list_feature_authorizations_custom,
            R.string.demo_activity_list_feature_authorizations_custom2,
            R.string.demo_activity_list_feature_devices_default,
            R.string.demo_activity_list_feature_devices_custom,
            R.string.demo_activity_list_feature_devices_custom2,
            R.string.demo_activity_list_feature_logs_default,
            R.string.demo_activity_list_feature_logs_custom,
            R.string.demo_activity_list_feature_logs_custom2,
            R.string.demo_activity_list_feature_totps_default,
            R.string.demo_activity_list_feature_totps_custom
    };

    private ListView mList;
    private DemoFeatureAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            updateToolbarTitle();
        }

        mAdapter = new DemoFeatureAdapter(this, FEATURES_RES);

        mList = (ListView) findViewById(R.id.list_listview);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
    }

    private void updateToolbarTitle() {
        if (getSupportActionBar() != null) {
            String status = "Device " + (getWhiteLabelManager().isDeviceLinked() ? "Linked" : "Unlinked");
            getSupportActionBar().setTitle(getString(R.string.demo_activity_list_title_format, status));
        }
    }

    //REGISTER AND UNREGISTER EVENT LISTENERS

    @Override
    protected void onResume() {
        super.onResume();
        updateToolbarTitle();
        getWhiteLabelManager().addAccountStateListener(this);
        getWhiteLabelManager().addStatusListener(this);
        getWhiteLabelManager().checkForActiveSessions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWhiteLabelManager().removeAccountStateListener(this);
        getWhiteLabelManager().removeStatusListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int featureStringId = mAdapter.getItem(position);

        boolean linked = getWhiteLabelManager().isDeviceLinked();

        String fragmentClassName = null;

        switch (featureStringId) {

            case R.string.demo_activity_list_feature_link_default_manual:

                if (linked) {
                    showError(ERROR_DEVICE_LINKED);
                } else {
                    getWhiteLabelManager().displayLinkingUi(this, WhiteLabelManager.LINKING_METHOD_MANUAL);
                }
                break;

            case R.string.demo_activity_list_feature_link_default_scanner:
                if (linked) {
                    showError(ERROR_DEVICE_LINKED);
                } else {
                    getWhiteLabelManager().displayLinkingUi(this, WhiteLabelManager.LINKING_METHOD_SCAN);
                }
                break;

            case R.string.demo_activity_list_feature_link_custom:
                if (linked) {
                    showError(ERROR_DEVICE_LINKED);
                } else {
                    fragmentClassName = CustomLinkingFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_requests:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = AuthRequestFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_logout_default:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    getWhiteLabelManager().logOut(this);
                }
                break;

            case R.string.demo_activity_list_feature_logout_custom:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomLogoutFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_unlink_default:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    getWhiteLabelManager().unlink(this);
                }
                break;

            case R.string.demo_activity_list_feature_unlink_custom:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomUnlinkFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_security:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    getWhiteLabelManager().displaySecurity(this);
                }
                break;

            case R.string.demo_activity_list_feature_authorizations_default:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = AuthorizationsFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_authorizations_custom:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomAuthorizationsFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_authorizations_custom2:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomAuthorizationsFragment2.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_devices_default:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = DevicesFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_devices_custom:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomDevicesFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_devices_custom2:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomDevicesFragment2.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_logs_default:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = LogsFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_logs_custom:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomLogsFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_logs_custom2:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomLogsFragment2.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_totps_default:

                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = TotpsFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_totps_custom:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = CustomTotpsFragment.class.getCanonicalName();
                }
                break;

            case R.string.demo_activity_list_feature_securityinfo:
                if (!linked) {
                    showError(ERROR_DEVICE_UNLINKED);
                } else {
                    fragmentClassName = SecurityInfoFragment.class.getCanonicalName();
                }
                break;
        }

        if (fragmentClassName != null) {

            //The full class name of a Fragment will be passed to the activity
            // so it's automatically instantiated by name and placed in a container.
            Intent fragmentActivity = new Intent(this, GenericFragmentDemoActivity.class);
            fragmentActivity.putExtra(GenericFragmentDemoActivity.EXTRA_FRAGMENT_CLASS, fragmentClassName);
            fragmentActivity.putExtra(GenericFragmentDemoActivity.EXTRA_TITLE, getString(featureStringId));

            startActivity(fragmentActivity);
        }
    }

    //SESSION-BASED EVENT(S)

    @Override
    public void onSessionUpdate(boolean hasActiveSessions) {
        if (hasActiveSessions) {
            showMessage("There are active sessions");
        }
    }

    //ACCOUNT-BASED EVENT(S)

    @Override
    public void onRequestUpdate(boolean hasPendingRequests) {
        showMessage("There are pending requests!");
    }

    @Override
    public void onAuthenticationSuccess(boolean approved) {
        String status = approved ? "Approved" : "Denied";
        showMessage("Request has been responded: ".concat(status));
    }

    @Override
    public void onAuthenticationFailure() {
        updateToolbarTitle();
        showError("Authentication failure");
    }

    @Override
    public void onUnlink() {
        updateToolbarTitle();
        showMessage("Device unlinked");
    }

    @Override
    public void onLogout() {
        showMessage("Logged out of active sessions");
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

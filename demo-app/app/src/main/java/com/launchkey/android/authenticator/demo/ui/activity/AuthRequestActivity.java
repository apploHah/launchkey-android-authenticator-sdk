package com.launchkey.android.authenticator.demo.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.demo.util.Utils;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.sdk.DeviceUnlinkedEventCallback;
import com.launchkey.android.authenticator.sdk.auth.AuthRequest;
import com.launchkey.android.authenticator.sdk.auth.AuthRequestManager;
import com.launchkey.android.authenticator.sdk.auth.event.AuthRequestResponseEventCallback;
import com.launchkey.android.authenticator.sdk.auth.event.GetAuthRequestEventCallback;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.authenticator.sdk.error.DeviceNotLinkedError;

/**
 * Created by armando on 8/9/16.
 */
public class AuthRequestActivity extends BaseDemoActivity {

    private AuthenticatorManager mAuthenticatorManager;
    private DeviceUnlinkedEventCallback mOnUnlink;
    private AuthRequestManager mAuthRequestManager;
    private GetAuthRequestEventCallback mGetAuthCallback;
    private AuthRequestResponseEventCallback mAuthResponseCallback;

    private TextView mNoRequestsView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_authrequest);

        mToolbar = (Toolbar) findViewById(R.id.demo_activity_authrequest_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Request");

        mNoRequestsView = (TextView) findViewById(R.id.demo_activity_authrequest_norequest);

        mAuthenticatorManager = AuthenticatorManager.getInstance();

        mOnUnlink = new DeviceUnlinkedEventCallback() {
            @Override
            public void onEventResult(boolean ok, BaseError e, Object o) {

                finish();
            }
        };

        mAuthRequestManager = AuthRequestManager.getInstance(this);

        mGetAuthCallback = new GetAuthRequestEventCallback() {

            @Override
            public void onEventResult(boolean successful, BaseError error, AuthRequest authRequest) {

                updateNoRequestsView(false);

                if (!successful) {
                    //let the callback handle the error
                    mAuthResponseCallback.onEventResult(false, error, null);
                }
            }
        };

        mAuthResponseCallback = new AuthRequestResponseEventCallback() {
            @Override
            public void onEventResult(boolean successful, BaseError error, Boolean authorized) {

                if (successful) {
                    mGetAuthCallback.onEventResult(true, null, null);
                } else {
                    if (error instanceof DeviceNotLinkedError) {
                        showUnlinkedDialog();
                    } else {
                        //this will potentially cover ExpiredAuthRequestError most of the time
                        Toast.makeText(AuthRequestActivity.this, Utils.getMessageForBaseError(error),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                finishIfNecessary();
            }
        };

        updateNoRequestsView(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_refresh:
                onRefresh();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthRequestManager.registerForEvents(mGetAuthCallback, mAuthResponseCallback);
        mAuthenticatorManager.registerForEvents(mOnUnlink);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthRequestManager.unregisterForEvents(mGetAuthCallback, mAuthResponseCallback);
        mAuthenticatorManager.unregisterForEvents(mOnUnlink);
    }

    @Override
    public void onBackPressed() {

        if (mAuthRequestManager.hasPending()) {
            return;
        }

        super.onBackPressed();
    }

    private void onRefresh() {
        updateNoRequestsView(true);
        mAuthRequestManager.check();
    }

    private void updateNoRequestsView(boolean isChecking) {

        int messageRes = isChecking ? R.string.demo_activity_authrequest_refreshing_message
                : R.string.demo_activity_authrequest_norequests_message;
        mNoRequestsView.setText(messageRes);

        boolean hasPending = mAuthRequestManager.getPendingAuthRequest() != null;

        mNoRequestsView.setVisibility(hasPending ? View.GONE : View.VISIBLE);
    }

    private void finishIfNecessary() {

        boolean hasPending = mAuthRequestManager.getPendingAuthRequest() != null;

        if (!hasPending) {

            finish();
        }
    }

    private void showUnlinkedDialog() {

        DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.demo_activity_authrequest_dialog_unlinked_title)
                .setMessage(R.string.demo_activity_authrequest_dialog_unlinked_message)
                .setPositiveButton(R.string.demo_generic_ok, positiveClick)
                .create();

        alertDialog.show();

        TextView messageView = (TextView) alertDialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
    }
}

package com.launchkey.android.whitelabel.demo.ui.activity;

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

import com.launchkey.android.authenticator.sdk.auth.AuthRequest;
import com.launchkey.android.authenticator.sdk.auth.AuthRequestManager;
import com.launchkey.android.authenticator.sdk.auth.event.AuthRequestResponseEventCallback;
import com.launchkey.android.authenticator.sdk.auth.event.GetAuthRequestEventCallback;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.authenticator.sdk.error.DeviceNotLinkedError;
import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.util.Utils;

/**
 * Created by armando on 8/9/16.
 */
public class AuthRequestActivity extends BaseDemoActivity {

    private AuthRequestManager mAuthRequestManager;
    private TextView mNoRequestsView;
    private Toolbar mToolbar;
    private GetAuthRequestEventCallback mGetAuthCallback;
    private AuthRequestResponseEventCallback mAuthResponseCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_authrequest);

        mToolbar = (Toolbar) findViewById(R.id.demo_activity_authrequest_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Request");

        mNoRequestsView = (TextView) findViewById(R.id.demo_activity_authrequest_norequest);

        mAuthRequestManager = AuthRequestManager.getInstance(this);

        mGetAuthCallback = new GetAuthRequestEventCallback() {
            @Override
            public void onEventResult(boolean successful, BaseError error, AuthRequest authRequest) {

                boolean hasPending = successful && authRequest != null;

                if (!hasPending) {
                    mNoRequestsView.setText("No pending requests");
                }

                if (successful) {
                    mNoRequestsView.setVisibility(hasPending ? View.GONE : View.VISIBLE);

                    if (hasPending) {
                        mToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
                        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onDeny();
                            }
                        });
                    } else {
                        mToolbar.setNavigationIcon(null);
                    }
                } else {
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
                        Toast.makeText(AuthRequestActivity.this, Utils.getMessageForBaseError(error), Toast.LENGTH_SHORT).show();
                    }
                }

                finish();
            }
        };
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthRequestManager.unregisterForEvents(mGetAuthCallback, mAuthResponseCallback);
    }

    private void onDeny() {
        mAuthRequestManager.denyAuthRequest();
    }

    private void onRefresh() {
        mNoRequestsView.setText("Checking...");
        mAuthRequestManager.check();
    }

    private void showUnlinkedDialog() {

        DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle(R.string.demo_activity_authrequest_dialog_unlinked_title)
                .setMessage(R.string.demo_activity_authrequest_dialog_unlinked_message)
                .setPositiveButton(R.string.demo_generic_ok, positiveClick)
                .create()
                .show();
    }
}

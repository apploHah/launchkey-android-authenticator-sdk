package com.launchkey.android.whitelabel.demo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;
import com.launchkey.android.whitelabel.sdk.ui.AuthRequestFragment;

/**
 * Created by armando on 8/9/16.
 */
public class AuthRequestActivity extends BaseDemoActivity implements WhiteLabelManager.AccountStateListener {

    private AuthRequestFragment mAuthRequest;
    private Toolbar mToolbar;
    private BroadcastReceiver mAuthRequestReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_authrequest);

        mToolbar = (Toolbar) findViewById(R.id.demo_activity_authrequest_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Request");

        mAuthRequest = (AuthRequestFragment) getSupportFragmentManager()
                .findFragmentById(R.id.demo_activity_authrequest_fragment);

        mAuthRequestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onRefresh();
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
        getWhiteLabelManager().addAccountStateListener(this);

        IntentFilter authRequestNotifFilter = new IntentFilter(WhiteLabelManager.ACTION_EVENT_REQUEST_INCOMING);
        LocalBroadcastManager.getInstance(this).registerReceiver(mAuthRequestReceiver, authRequestNotifFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWhiteLabelManager().removeAccountStateListener(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mAuthRequestReceiver);
    }

    private void onDeny() {
        mAuthRequest.deny();
    }

    private void onRefresh() {
        mAuthRequest.checkForPending();
    }

    @Override
    public void onAuthenticationSuccess(boolean approved) {
        onRequestUpdate(false);
        finish();
    }

    @Override
    public void onRequestUpdate(boolean hasPending) {

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
    }

    @Override
    public void onAuthenticationFailure() {}

    @Override
    public void onUnlink() {}

    @Override
    public void onLogout() {}
}

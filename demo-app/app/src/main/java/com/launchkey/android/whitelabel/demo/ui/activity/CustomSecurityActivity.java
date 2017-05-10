package com.launchkey.android.whitelabel.demo.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.launchkey.android.authenticator.sdk.ui.SecurityFragment;
import com.launchkey.android.whitelabel.demo.R;

/**
 * Created by armando on 5/9/17.
 */

public class CustomSecurityActivity extends BaseDemoActivity {

    private SecurityFragment mSecFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_security);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.sec_toolbar);
        setSupportActionBar(toolbar);

        mSecFragment = (SecurityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.sec_security);
    }
}

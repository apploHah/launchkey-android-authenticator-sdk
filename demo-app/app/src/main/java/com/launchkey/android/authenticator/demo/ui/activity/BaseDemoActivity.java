package com.launchkey.android.authenticator.demo.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.demo.ui.BaseDemoView;

/**
 * Created by armando on 7/8/16.
 */
public class BaseDemoActivity extends AppCompatActivity implements BaseDemoView {

    private AuthenticatorManager mAuthenticatorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthenticatorManager = AuthenticatorManager.getInstance();
    }

    @Override
    public AuthenticatorManager getAuthenticatorManager() {
        return mAuthenticatorManager;
    }
}

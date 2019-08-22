package com.launchkey.android.authenticator.demo.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.launchkey.android.authenticator.demo.ui.BaseDemoView;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;

/**
 * Created by armando on 7/8/16.
 */
public class BaseDemoActivity extends AppCompatActivity implements BaseDemoView {

    private final AuthenticatorManager mAuthenticatorManager = AuthenticatorManager.getInstance();

    @Override
    public AuthenticatorManager getAuthenticatorManager() {
        return mAuthenticatorManager;
    }
}

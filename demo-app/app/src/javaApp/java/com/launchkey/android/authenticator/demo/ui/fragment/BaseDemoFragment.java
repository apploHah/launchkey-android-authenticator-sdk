package com.launchkey.android.authenticator.demo.ui.fragment;

import android.support.v4.app.Fragment;

import com.launchkey.android.authenticator.demo.ui.BaseDemoView;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;

/**
 * Created by armando on 7/8/16.
 */
public class BaseDemoFragment extends Fragment implements BaseDemoView {

    private AuthenticatorManager mAuthenticatorManager = AuthenticatorManager.getInstance();

    @Override
    public AuthenticatorManager getAuthenticatorManager() {
        return mAuthenticatorManager;
    }
}

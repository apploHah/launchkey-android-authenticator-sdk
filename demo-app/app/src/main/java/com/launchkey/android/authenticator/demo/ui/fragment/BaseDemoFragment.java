package com.launchkey.android.authenticator.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.demo.ui.BaseDemoView;

/**
 * Created by armando on 7/8/16.
 */
public class BaseDemoFragment extends Fragment implements BaseDemoView {

    private AuthenticatorManager mAuthenticatorManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuthenticatorManager = AuthenticatorManager.getInstance();
    }

    @Override
    public AuthenticatorManager getAuthenticatorManager() {
        return mAuthenticatorManager;
    }
}

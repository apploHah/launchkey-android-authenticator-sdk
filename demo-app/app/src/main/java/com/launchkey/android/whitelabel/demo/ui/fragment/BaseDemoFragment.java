package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.launchkey.android.whitelabel.demo.ui.BaseDemoView;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;

/**
 * Created by armando on 7/8/16.
 */
public class BaseDemoFragment extends Fragment implements BaseDemoView {

    private WhiteLabelManager mWhiteLabelManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWhiteLabelManager = WhiteLabelManager.getInstance();
    }

    @Override
    public WhiteLabelManager getWhiteLabelManager() {
        return mWhiteLabelManager;
    }
}

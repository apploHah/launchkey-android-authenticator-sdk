package com.launchkey.android.whitelabel.demo.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.launchkey.android.whitelabel.demo.ui.BaseDemoView;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;

/**
 * Created by armando on 7/8/16.
 */
public class BaseDemoActivity extends AppCompatActivity implements BaseDemoView {

    private WhiteLabelManager mWhiteLabelManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWhiteLabelManager = WhiteLabelManager.getInstance();
    }

    @Override
    public WhiteLabelManager getWhiteLabelManager() {
        return mWhiteLabelManager;
    }
}

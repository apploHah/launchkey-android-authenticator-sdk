package com.launchkey.android.authenticator.demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.sdk.DeviceUnlinkedEventCallback;
import com.launchkey.android.authenticator.sdk.error.BaseError;

/**
 * Created by armando on 7/8/16.
 */
public class GenericFragmentDemoActivity extends BaseDemoActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_FRAGMENT_CLASS = "fragment_class";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.demo_activity_fragment_toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle extras = i == null ? null : i.getExtras();

        if (getSupportActionBar() != null) {

            String title = extras == null ? null : extras.getString(EXTRA_TITLE);
            getSupportActionBar().setTitle(title == null ? "Demo" : title);
        }

        if (savedInstanceState != null) {
            return;
        }

        String fragmentClass = extras == null ? null : extras.getString(EXTRA_FRAGMENT_CLASS);

        if (fragmentClass == null) {
            finish();
            return;
        }

        //Instantiate the Fragment by name that was passed via extra (Bundle) and if not null,
        // then place it in the container.

        Fragment f;

        try {
            f = Fragment.instantiate(this, fragmentClass);
        } catch (Exception e) {
            finish();
            return;
        }

        if (f != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.demo_activity_fragment_container, f)
                    .commit();
        }

        getAuthenticatorManager().registerForEvents(new DeviceUnlinkedEventCallback() {
            @Override
            public void onEventResult(boolean b, BaseError baseError, Object o) {
                finish();
            }
        });
    }
}

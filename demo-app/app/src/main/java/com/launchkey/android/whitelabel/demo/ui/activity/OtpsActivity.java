package com.launchkey.android.whitelabel.demo.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.launchkey.android.authenticator.sdk.ui.fragment.CodesFragment;
import com.launchkey.android.whitelabel.demo.R;

/**
 * Created by armando on 8/17/16.
 */
public class OtpsActivity extends BaseDemoActivity {

    private CodesFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_otps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.demo_activity_otps_toolbar);
        setSupportActionBar(toolbar);

        mFragment = (CodesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.demo_activity_otps_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:
                mFragment.showAddOptions();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

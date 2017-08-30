package com.launchkey.android.whitelabel.demo.push;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by armando on 8/14/17.
 */

public class PushIdInstanceListenerService extends InstanceIDListenerService {

    private static final String TAG = PushIdInstanceListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        Log.i(TAG, "onTokenRefresh()");
        Intent registration = new Intent(this, PushRegIntentService.class);
        startService(registration);
    }
}


package com.launchkey.android.authenticator.demo.push;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;

/**
 * Created by armando on 8/14/17.
 */

public class PushListenerService extends GcmListenerService {

    private static final String TAG = PushListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i(TAG, "Push notification received from=" + from + " data=" + data);
        // Deprecated: //AuthenticatorManager.getInstance().onPushNotification(data, from);
        AuthenticatorManager.getInstance().onPushNotification(data);
    }
}

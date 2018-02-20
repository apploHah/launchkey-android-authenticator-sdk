package com.launchkey.android.authenticator.demo.push;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.demo.R;

/**
 * Created by armando on 8/14/17.
 */

public class PushRegIntentService extends IntentService {

    private static final String TAG = PushRegIntentService.class.getSimpleName();

    public PushRegIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            InstanceID instanceId = InstanceID.getInstance(this);
            final String token = instanceId.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            AuthenticatorManager.getInstance().updatePushNotificationToken(token);
            Log.i(TAG, "Device token obtained token=" + token);
        } catch (Exception e) {
            Log.e(TAG, "Error obtaining device token for push notifications.", e);
        }
    }
}

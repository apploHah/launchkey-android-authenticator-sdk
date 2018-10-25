package com.launchkey.android.authenticator.demo.push;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.launchkey.android.authenticator.sdk.auth.AuthRequestManager;

public class PushMessagingService extends FirebaseMessagingService {

    private static final String TAG = PushMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i(TAG, "RemoteMessage=" + remoteMessage.getData());

        AuthRequestManager.getInstance(this).check();
    }
}

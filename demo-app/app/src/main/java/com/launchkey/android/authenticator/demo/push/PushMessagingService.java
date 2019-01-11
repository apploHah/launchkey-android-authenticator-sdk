package com.launchkey.android.authenticator.demo.push;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.launchkey.android.authenticator.demo.app.Notifier;

public class PushMessagingService extends FirebaseMessagingService {

    private static final String TAG = PushMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null) {
            Log.e(TAG, "RemoteMessage is null.");
            return;
        }

        Log.i(TAG, "RemoteMessage.Data=" + remoteMessage.getData());

        Notifier.getInstance(this).notifyOfRequest(remoteMessage.getNotification());
    }
}

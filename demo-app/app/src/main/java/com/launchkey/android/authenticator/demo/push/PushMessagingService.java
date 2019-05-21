package com.launchkey.android.authenticator.demo.push;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.launchkey.android.authenticator.demo.app.Notifier;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;

public class PushMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);

        Log.i(PushMessagingService.class.getSimpleName(), "newToken=" + newToken);
        // If this method is called, the push token has changed and the LK API
        // has to be notified.
        AuthenticatorManager.getInstance().updatePushNotificationToken(newToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null) {
            return;
        }

        Log.i(PushMessagingService.class.getSimpleName(), "newPush=" + remoteMessage);

        if (remoteMessage.getData() != null) {

            Log.i(PushMessagingService.class.getSimpleName(), "newPushData=" + remoteMessage.getData());
        }

        // Create a notification based on pre-existing notification properties
        Notifier.getInstance(this).notifyOfRequest(remoteMessage.getNotification());

        // If app is handling push notifications (older versions of
        // Android or in the foreground) then hand the payload to the
        // Authenticator SDK.
        AuthenticatorManager.getInstance().onPushNotification(remoteMessage.getData());
    }
}

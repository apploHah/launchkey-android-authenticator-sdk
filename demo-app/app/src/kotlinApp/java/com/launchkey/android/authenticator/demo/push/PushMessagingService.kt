package com.launchkey.android.authenticator.demo.push

import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.launchkey.android.authenticator.demo.app.Notifier
import com.launchkey.android.authenticator.sdk.AuthenticatorManager

class PushMessagingService : FirebaseMessagingService() {

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)

        Log.i(PushMessagingService::class.java.simpleName, "newToken=" + newToken!!)
        // If this method is called, the push token has changed and the LK API
        // has to be notified.
        AuthenticatorManager.getInstance().updatePushNotificationToken(newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage == null) {
            return
        }

        Log.i(PushMessagingService::class.java.simpleName, "newPush=$remoteMessage")

        if (remoteMessage.data != null) {

            Log.i(PushMessagingService::class.java.simpleName, "newPushData=" + remoteMessage.data)
        }

        // Create a notification based on pre-existing notification properties
        Notifier.getInstance(this)!!.notifyOfRequest(remoteMessage.notification)

        // If app is handling push notifications (older versions of
        // Android or in the foreground) then hand the payload to the
        // Authenticator SDK.
        AuthenticatorManager.getInstance().onPushNotification(remoteMessage.data)
    }
}

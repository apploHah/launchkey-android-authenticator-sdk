package com.launchkey.android.authenticator.demo.push;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;

public class PushInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // If this method is called, the push token has changed and the LK API has to be notified
        String newToken = FirebaseInstanceId.getInstance().getToken();
        AuthenticatorManager.getInstance().updatePushNotificationToken(newToken);
    }
}

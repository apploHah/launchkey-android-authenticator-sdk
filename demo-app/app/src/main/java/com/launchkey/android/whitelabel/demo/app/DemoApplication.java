package com.launchkey.android.whitelabel.demo.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.launchkey.android.authenticator.sdk.AuthenticatorApplication;
import com.launchkey.android.authenticator.sdk.AuthenticatorConfig;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.sdk.DeviceLinkedEventCallback;
import com.launchkey.android.authenticator.sdk.DeviceUnlinkedEventCallback;
import com.launchkey.android.authenticator.sdk.auth.AuthRequest;
import com.launchkey.android.authenticator.sdk.auth.AuthRequestManager;
import com.launchkey.android.authenticator.sdk.auth.event.GetAuthRequestEventCallback;
import com.launchkey.android.authenticator.sdk.device.Device;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.ui.activity.ListDemoActivity;

import java.util.Locale;

public class DemoApplication extends AuthenticatorApplication {

    public static final String TAG = DemoApplication.class.getSimpleName();

    private static final int NOTIFICATION_ID = 100;

    private static NotificationManagerCompat mNotificationManager;

    @Override
    public void onCreate() {
        initialize();
        super.onCreate();
    }

    private void initialize() {
        int keyPairSizeBits = AuthenticatorConfig.Builder.KEYSIZE_MINIMUM;
        //int keyPairSizeBits = WhiteLabelConfig.Builder.KEYSIZE_MEDIUM;
        //keyPairSizeBits = 3072; //Could also assign the actual value in bits.

        final AuthenticatorManager manager = AuthenticatorManager.getInstance();

        manager.initialize(
                        new AuthenticatorConfig.Builder(this, R.string.authenticator_sdk_key)
                                .keyPairSize(keyPairSizeBits)
                                .theme(R.style.DemoAppTheme)
                                .build());

        manager.registerForEvents(

                new DeviceLinkedEventCallback() {
                    @Override
                    public void onEventResult(boolean b, BaseError baseError, Device device) {
                        Log.i(TAG, String.format(Locale.getDefault(), "Link-event=%b Device-name=%s", b, device.getName()));
                    }
                },

                new DeviceUnlinkedEventCallback() {
                    @Override
                    public void onEventResult(boolean b, BaseError baseError, Object o) {
                        Log.i(TAG, String.format(Locale.getDefault(), "Unlink-event=%b error=%s", b, baseError));
                    }
                }
        );

        if (manager.isDeviceLinked()) {

            final AuthRequestManager arm = AuthRequestManager.getInstance(this);
            arm.registerForEvents(new GetAuthRequestEventCallback() {

                @Override
                public void onEventResult(boolean successful, BaseError error, AuthRequest authRequest) {

                    if (authRequest != null) {
                        notifyOfRequest();
                    }
                }
            });
        }
    }

    private void notifyOfRequest() {
        if (mNotificationManager == null) {
            mNotificationManager = NotificationManagerCompat.from(this);
        }

        Intent tapIntent = new Intent(this, ListDemoActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        tapIntent.putExtra(ListDemoActivity.EXTRA_SHOW_REQUEST, true);

        PendingIntent tapPendingIntent = PendingIntent.getActivity(this, 1, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_orange)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notif_request_message))
                .setContentIntent(tapPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();


        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void cancelRequestNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
    }
}

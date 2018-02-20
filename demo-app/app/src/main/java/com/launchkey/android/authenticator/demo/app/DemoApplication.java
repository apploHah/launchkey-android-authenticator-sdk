package com.launchkey.android.authenticator.demo.app;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.launchkey.android.authenticator.sdk.AuthenticatorConfig;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.sdk.DeviceKeyPairGeneratedEventCallback;
import com.launchkey.android.authenticator.sdk.DeviceLinkedEventCallback;
import com.launchkey.android.authenticator.sdk.DeviceUnlinkedEventCallback;
import com.launchkey.android.authenticator.sdk.auth.AuthRequest;
import com.launchkey.android.authenticator.sdk.auth.AuthRequestManager;
import com.launchkey.android.authenticator.sdk.auth.event.GetAuthRequestEventCallback;
import com.launchkey.android.authenticator.sdk.device.Device;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.demo.push.PushRegIntentService;
import com.launchkey.android.authenticator.demo.ui.activity.ListDemoActivity;
import com.launchkey.android.authenticator.demo.util.Utils;

import java.util.Locale;

public class DemoApplication extends Application {

    public static final String TAG = DemoApplication.class.getSimpleName();
    public static final boolean CONFIG_ALLOW_LAR = true;

    private static final int NOTIFICATION_ID = 100;

    private static NotificationManagerCompat mNotificationManager;

    @Override
    public void onCreate() {

        initialize();

        // Kick-start push registration service
        startService(new Intent(this, PushRegIntentService.class));

        super.onCreate();
    }

    private void initialize() {
        int keyPairSizeBits = AuthenticatorConfig.Builder.KEYSIZE_MINIMUM;
        //keyPairSizeBits = 3072; //Could also assign the actual value in bits.

        final AuthenticatorManager manager = AuthenticatorManager.getInstance();

        final int geofencingDelaySeconds = 20 * 60;     //20 minutes
        final int proximityDelaySeconds = 10 * 60;      //10 minutes

        manager.initialize(
                        new AuthenticatorConfig.Builder(this, R.string.authenticator_sdk_key)
                                .activationDelayGeofencing(geofencingDelaySeconds)
                                .activationDelayProximity(proximityDelaySeconds)
                                .keyPairSize(keyPairSizeBits)
                                .theme(R.style.DemoAppTheme)
                                .allowSecurityChangesWhenUnlinked(CONFIG_ALLOW_LAR)
                                //.customFont("fonts/ostrich-regular.ttf")
                                .build());

        manager.registerForEvents(

                new DeviceLinkedEventCallback() {
                    @Override
                    public void onEventResult(boolean b, BaseError baseError, Device device) {

                        final String deviceName = b ? device.getName() : null;
                        Log.i(TAG, String.format(Locale.getDefault(), "Link-event=%b Device-name=%s", b, deviceName));
                    }
                },

                new DeviceUnlinkedEventCallback() {
                    @Override
                    public void onEventResult(boolean b, BaseError baseError, Object o) {
                        Log.i(TAG, String.format(Locale.getDefault(), "Unlink-event=%b error=%s", b, baseError));
                    }
                },

                new DeviceKeyPairGeneratedEventCallback() {
                    @Override
                    public void onEventResult(boolean b, BaseError baseError, Object o) {
                        Log.i(TAG, "Device key pair now generated.");
                    }
                }
        );

        final AuthRequestManager arm = AuthRequestManager.getInstance(this);
        arm.registerForEvents(new GetAuthRequestEventCallback() {

            @Override
            public void onEventResult(boolean successful, BaseError error, AuthRequest authRequest) {
                Log.i(TAG, "Auth Request Check s=" + successful + " err=" + Utils.getMessageForBaseError(error) + " ar=" + authRequest);
                if (authRequest != null) {
                    notifyOfRequest();
                }
            }
        });
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

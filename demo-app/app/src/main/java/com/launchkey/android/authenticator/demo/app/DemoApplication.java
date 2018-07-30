package com.launchkey.android.authenticator.demo.app;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.demo.push.PushRegIntentService;
import com.launchkey.android.authenticator.demo.ui.activity.ListDemoActivity;
import com.launchkey.android.authenticator.demo.util.Utils;
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
import com.launchkey.android.authenticator.sdk.ui.theme.AuthenticatorTheme;

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

        final int geofencingDelaySeconds = 60;
        final int proximityDelaySeconds = 30;

        final AuthenticatorTheme theme = new AuthenticatorTheme.Builder(this)
                .appBar(
                        ContextCompat.getColor(this, R.color.demo_appbar_bg),
                        ContextCompat.getColor(this, R.color.demo_appbar_overlay))
                .listHeaders(
                        View.VISIBLE,
                        ContextCompat.getColor(this, R.color.transparent),
                        ContextCompat.getColor(this, R.color.demo_backgrounds_overlay))
                .listItems(
                        Color.argb(100, 255, 255, 255),
                        ContextCompat.getColor(this, R.color.demo_color_gray_dark))
                .background(
                        new ColorDrawable(ContextCompat.getColor(this, R.color.demo_backgrounds)))
                .backgroundOverlay(
                        ContextCompat.getColor(this, R.color.demo_backgrounds_overlay))
                .settingsHeaders(
                        ContextCompat.getColor(this, R.color.transparent),
                        ContextCompat.getColor(this, R.color.demo_color_gray_dark))
                .helpMenuItems(
                        false)
                .buttonNegative(
                        new ColorDrawable(ContextCompat.getColor(this, R.color.demo_color_negative)),
                        Color.WHITE)
                .authSlider(
                        ContextCompat.getColor(this, R.color.demo_slider_track_upper),
                        ContextCompat.getColor(this, R.color.demo_slider_track_lower),
                        ContextCompat.getColor(this, R.color.demo_slider_thumb_normal),
                        ContextCompat.getColor(this, R.color.demo_slider_thumb_pressed),
                        ContextCompat.getColor(this, R.color.demo_color_gray_dark))
                .pinCode(
                        ContextCompat.getDrawable(this, R.drawable.pinpad_button_bg),
                        ContextCompat.getColorStateList(this, R.color.pinpad_button_text))
                .circleCode(
                        ContextCompat.getColor(this, R.color.demo_circlecode_highlight),
                        ContextCompat.getColor(this, R.color.demo_circlecode_marks))
                .geoFence(
                        ContextCompat.getColor(this, R.color.demo_circlecode_highlight))
                // Other properties before building:
                //.factorsSecurityIcons(View.VISIBLE, R.drawable.ic_photo_camera_white_24dp, R.drawable.ic_clear_white_24dp, R.drawable.ic_help_black_24dp, 0, 0, ContextCompat.getColor(this, R.color.lk_otp_tokens_orange))
                //.editText(ContextCompat.getColor(this, R.color.lk_otp_tokens_orange), ContextCompat.getColor(this, R.color.lk_otp_tokens_blue))
                //.factorsBusyIcons(R.drawable.ic_check_black_48dp, R.drawable.ic_check_black_48dp, R.drawable.ic_check_black_48dp)
                .build();

        final AuthenticatorManager manager = AuthenticatorManager.getInstance();

        manager.initialize(
                        new AuthenticatorConfig.Builder(this, R.string.authenticator_sdk_key)
                                .activationDelayGeofencing(geofencingDelaySeconds)
                                .activationDelayProximity(proximityDelaySeconds)
                                .keyPairSize(keyPairSizeBits)
                                // Providing static (XML) and dynamic (Java) themes will provide
                                // properties only expected in XML along with other resources
                                // at runtime:
                                .theme(R.style.DemoAppTheme)
                                .theme(theme)
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

package com.launchkey.android.authenticator.demo.app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.demo.util.KeyStorage;
import com.launchkey.android.authenticator.sdk.AuthenticatorConfig;
import com.launchkey.android.authenticator.sdk.AuthenticatorManager;
import com.launchkey.android.authenticator.sdk.DeviceKeyPairGeneratedEventCallback;
import com.launchkey.android.authenticator.sdk.DeviceLinkedEventCallback;
import com.launchkey.android.authenticator.sdk.DeviceUnlinkedEventCallback;
import com.launchkey.android.authenticator.sdk.SimpleOperationCallback;
import com.launchkey.android.authenticator.sdk.device.Device;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.authenticator.sdk.ui.theme.AuthenticatorTheme;

import java.util.Locale;

public class DemoApplication extends MultiDexApplication {

    public static final String TAG = DemoApplication.class.getSimpleName();

    private AuthenticatorManager manager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize(null);
    }

    public void initialize(String sdkKey) {

        int keyPairSizeBits = AuthenticatorConfig.Builder.KEYSIZE_MINIMUM;
        //int keyPairSizeBits = AuthenticatorConfig.Builder.KEYSIZE_MEDIUM;
        //keyPairSizeBits = 3072; //Could also assign the actual value in bits.

        final int geofencingDelaySeconds = 60;
        final int proximityDelaySeconds = 60;

        AuthenticatorTheme customTheme = new AuthenticatorTheme.Builder(this)
                .appBar(Color.DKGRAY, Color.BLUE)
                .authRequestAppBar(View.GONE)
                .listHeaders(View.VISIBLE, Color.argb(100, 0, 0, 0), Color.CYAN)
                .listItems(Color.argb(100, 0, 0, 0), Color.CYAN)
                .background(new ColorDrawable(Color.DKGRAY))
                .backgroundOverlay(Color.LTGRAY)
                .settingsHeaders(Color.BLUE, Color.WHITE)
                .factorsSecurityIcons(View.VISIBLE, R.drawable.ic_photo_camera_white_24dp, R.drawable.ic_clear_white_24dp, R.drawable.ic_help_black_24dp, 0, 0, ContextCompat.getColor(this, R.color.demo_generic_orange))
                .listItems(Color.parseColor("#7B5F49"), ContextCompat.getColor(this, R.color.demo_generic_orange))
                .factorsBusyIcons(R.drawable.ic_check_black_48dp, R.drawable.ic_check_black_48dp, R.drawable.ic_check_black_48dp)
                .helpMenuItems(false)
                .button(ContextCompat.getDrawable(this, R.drawable.pinpad_button_bg), Color.WHITE)
                .buttonNegative(new ColorDrawable(Color.RED), Color.CYAN)
                .authSlider(Color.YELLOW, Color.BLUE, Color.RED, Color.GREEN, Color.BLACK)
                .circleCode(Color.MAGENTA, Color.GREEN)
                .pinCode(ContextCompat.getDrawable(this, R.drawable.lk_textbutton_background), Color.YELLOW)
                .geoFence(ContextCompat.getColor(this, R.color.demo_generic_orange))
                .editText(ContextCompat.getColor(this, R.color.demo_generic_orange), ContextCompat.getColor(this, R.color.demo_generic_blue))
                .expirationTimer(ContextCompat.getColor(this, R.color.demo_generic_green), ContextCompat.getColor(this, R.color.demo_generic_blue), ContextCompat.getColor(this, R.color.demo_generic_orange))
                .denialReasons(ContextCompat.getColor(this, R.color.demo_generic_blue), ContextCompat.getColor(this, R.color.demo_generic_orange))
                .authResponseButton(R.drawable.arb_bg_positive, android.R.color.white, R.color.arb_positive_fill)
                .authResponseButtonNegative(R.drawable.arb_bg_negative, android.R.color.white, R.color.arb_negative_fill)
                .authResponseAuthorizedColor(Color.parseColor("#a8ff00"))
                .authResponseDeniedColor(Color.parseColor("#c000ff"))
                .authResponseFailedColor(ContextCompat.getColor(this, R.color.demo_generic_orange))
                .authContentViewBackground(Color.parseColor("#00695C"))
                .build();

        final String finalSdkKey = sdkKey != null ? sdkKey : getString(R.string.authenticator_sdk_key);

        AuthenticatorConfig config = new AuthenticatorConfig.Builder(this, finalSdkKey)
                .activationDelayGeofencing(geofencingDelaySeconds)
                .activationDelayProximity(proximityDelaySeconds)
                .keyPairSize(keyPairSizeBits)
                .theme(R.style.DemoAppTheme) // Built theme programmatically in the next line
                //.theme(customTheme)
                .build();

        manager = AuthenticatorManager.getInstance();
        manager.initialize(config);

        KeyStorage.getInstance(this).setKey(finalSdkKey);

        DeviceLinkedEventCallback onDeviceLink = new DeviceLinkedEventCallback() {

            @Override
            public void onEventResult(boolean b, BaseError baseError, Device device) {

                final String deviceName = b ? device.getName() : null;
                Log.i(TAG, String.format(Locale.getDefault(), "Link-event=%b Device-name=%s", b, deviceName));
            }
        };

        DeviceUnlinkedEventCallback onDeviceUnlink = new DeviceUnlinkedEventCallback() {

            @Override
            public void onEventResult(boolean b, BaseError baseError, Object o) {

                Log.i(TAG, String.format(Locale.getDefault(), "Unlink-event=%b error=%s", b, baseError));
            }
        };

        DeviceKeyPairGeneratedEventCallback onDeviceKey = new DeviceKeyPairGeneratedEventCallback() {

            @Override
            public void onEventResult(boolean b, BaseError baseError, Object o) {

                Log.i(TAG, "Device key pair now generated.");
            }
        };

        manager.registerForEvents(onDeviceLink, onDeviceUnlink, onDeviceKey);

        if (manager.isDeviceLinked()) {
            Log.i(TAG, "Linked. About to send metrics...");
            manager.sendMetrics(new SimpleOperationCallback<Void>() {

                @Override
                public void onResult(boolean ok, BaseError e, Void v) {

                    Log.i(TAG, "Metrics delivery. OK=" + ok + " E=" + e);
                }
            });
        }
    }
}

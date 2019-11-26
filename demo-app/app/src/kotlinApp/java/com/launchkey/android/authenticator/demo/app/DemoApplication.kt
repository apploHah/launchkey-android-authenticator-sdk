package com.launchkey.android.authenticator.demo.app

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.launchkey.android.authenticator.demo.R
import com.launchkey.android.authenticator.demo.util.KeyStorage
import com.launchkey.android.authenticator.sdk.*
import com.launchkey.android.authenticator.sdk.device.Device
import com.launchkey.android.authenticator.sdk.error.BaseError
import com.launchkey.android.authenticator.sdk.ui.theme.AuthenticatorTheme
import java.util.*

class DemoApplication : Application() {

    private var manager: AuthenticatorManager? = null

    override fun onCreate() {
        super.onCreate()
        initialize(null)
    }

    fun initialize(sdkKey: String?) {

        val keyPairSizeBits = AuthenticatorConfig.Builder.KEYSIZE_MINIMUM
        //int keyPairSizeBits = AuthenticatorConfig.Builder.KEYSIZE_MEDIUM;
        //keyPairSizeBits = 3072; //Could also assign the actual value in bits.

        val geofencingDelaySeconds = 60
        val proximityDelaySeconds = 60

        val customTheme = AuthenticatorTheme.Builder(this)
                .appBar(Color.DKGRAY, Color.BLUE)
                .authRequestAppBar(View.GONE)
                .listHeaders(View.VISIBLE, Color.argb(100, 0, 0, 0), Color.CYAN)
                .listItems(Color.argb(100, 0, 0, 0), Color.CYAN)
                .background(ColorDrawable(Color.DKGRAY))
                .backgroundOverlay(Color.LTGRAY)
                .settingsHeaders(Color.BLUE, Color.WHITE)
                .factorsSecurityIcons(View.VISIBLE, R.drawable.ic_photo_camera_white_24dp, R.drawable.ic_clear_white_24dp, R.drawable.ic_help_black_24dp, 0, 0, ContextCompat.getColor(this, R.color.demo_generic_orange))
                .listItems(Color.parseColor("#7B5F49"), ContextCompat.getColor(this, R.color.demo_generic_orange))
                .factorsBusyIcons(R.drawable.ic_check_black_48dp, R.drawable.ic_check_black_48dp, R.drawable.ic_check_black_48dp)
                .helpMenuItems(false)
                .button(ContextCompat.getDrawable(this, R.drawable.pinpad_button_bg), Color.WHITE)
                .buttonNegative(ColorDrawable(Color.RED), Color.CYAN)
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
                .build()

        val finalSdkKey = sdkKey ?: getString(R.string.authenticator_sdk_key)

        val config = AuthenticatorConfig.Builder(this, finalSdkKey)
                .activationDelayGeofencing(geofencingDelaySeconds)
                .activationDelayProximity(proximityDelaySeconds)
                .keyPairSize(keyPairSizeBits)
                .theme(R.style.DemoAppTheme) // Built theme programmatically in the next line
                //.theme(customTheme)
                .build()

        manager = AuthenticatorManager.getInstance()
        manager!!.initialize(config)

        KeyStorage.getInstance(this)!!.key = finalSdkKey

        val onDeviceLink = object : DeviceLinkedEventCallback() {

            override fun onEventResult(b: Boolean, baseError: BaseError?, device: Device?) {

                val deviceName = if (b) device?.name else null
                Log.i(TAG, String.format(Locale.getDefault(), "Link-event=%b Device-name=%s", b, deviceName))
            }
        }

        val onDeviceUnlink = object : DeviceUnlinkedEventCallback() {

            override fun onEventResult(b: Boolean, baseError: BaseError?, o: Any?) {

                Log.i(TAG, String.format(Locale.getDefault(), "Unlink-event=%b error=%s", b, baseError))
            }
        }

        val onDeviceKey = object : DeviceKeyPairGeneratedEventCallback() {

            override fun onEventResult(b: Boolean, baseError: BaseError?, o: Any?) {

                Log.i(TAG, "Device key pair now generated.")
            }
        }

        manager!!.registerForEvents(onDeviceLink, onDeviceUnlink, onDeviceKey)

        if (manager!!.isDeviceLinked) {
            Log.i(TAG, "Linked. About to send metrics...")
            manager!!.sendMetrics { ok, e, v -> Log.i(TAG, "Metrics delivery. OK=$ok E=$e") }
        }
    }

    companion object {

        val TAG = DemoApplication::class.java.simpleName
    }
}

package com.launchkey.android.authenticator.demo.ui.activity

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView

import com.launchkey.android.authenticator.demo.R
import com.launchkey.android.authenticator.demo.util.KeyStorage
import com.launchkey.android.authenticator.sdk.AuthenticatorConfig
import com.launchkey.android.authenticator.sdk.SimpleOperationCallback
import com.launchkey.android.authenticator.sdk.error.BaseError
import com.launchkey.android.authenticator.sdk.security.SecurityService

class AppConfigsActivity : BaseDemoActivity() {


    private var mPinCode: Switch? = null
    private var mCircleCode: Switch? = null
    private var mWearables: Switch? = null
    private var mLocations: Switch? = null
    private var mFingerprintScan: Switch? = null
    private var mDelayWearables: EditText? = null
    private var mDelayLocations: EditText? = null
    private var mAuthFailure: EditText? = null
    private var mAutoUnlink: EditText? = null
    private var mAutoUnlinkWarning: EditText? = null
    private var mAllowChangesWhenUnlinked: Switch? = null
    private var mEndpoint: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_configs)

        mPinCode = findViewById(R.id.configs_pc)
        mCircleCode = findViewById(R.id.configs_cc)
        mWearables = findViewById(R.id.configs_w)
        mLocations = findViewById(R.id.configs_l)
        mFingerprintScan = findViewById(R.id.configs_fs)

        mDelayWearables = findViewById(R.id.configs_delay_wearables)
        mDelayLocations = findViewById(R.id.configs_delay_locations)

        mAuthFailure = findViewById(R.id.configs_authfailure)
        mAutoUnlink = findViewById(R.id.configs_autounlink)
        mAutoUnlinkWarning = findViewById(R.id.configs_autounlinkwarning)

        mAllowChangesWhenUnlinked = findViewById(R.id.configs_allowchangesunlinked)
        mEndpoint = findViewById(R.id.configs_endpoint)

        findViewById<View>(R.id.configs_button).setOnClickListener { onReinitSdk() }

        updateUi()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {

        // Prep all hints at runtime

        val delaySecondsMin = 0
        val delaySecondsMax = 60 * 60 * 24
        val delayHints = getString(
                R.string.demo_activity_list_feature_config_hints_format, delaySecondsMin, delaySecondsMax)
        mDelayWearables!!.hint = delayHints
        mDelayLocations!!.hint = delayHints

        val authFailureHint = getString(
                R.string.demo_activity_list_feature_config_hints_format,
                AuthenticatorConfig.Builder.THRESHOLD_AUTHFAILURE_MINIMUM,
                AuthenticatorConfig.Builder.THRESHOLD_AUTHFAILURE_MAXIMUM)
        mAuthFailure!!.hint = authFailureHint

        val autoUnlinkHint = getString(
                R.string.demo_activity_list_feature_config_hints_format,
                AuthenticatorConfig.Builder.THRESHOLD_AUTOUNLINK_MINIMUM,
                AuthenticatorConfig.Builder.THRESHOLD_AUTOUNLINK_MAXIMUM)
        mAutoUnlink!!.hint = autoUnlinkHint

        // For the warning, max is derived from Auto Unlink max - 1
        val autoUnlinkWarningHint = getString(
                R.string.demo_activity_list_feature_config_hints_format,
                AuthenticatorConfig.Builder.THRESHOLD_AUTOUNLINK_WARNING_NONE,
                AuthenticatorConfig.Builder.THRESHOLD_AUTOUNLINK_MAXIMUM - 1)
        mAutoUnlinkWarning!!.hint = autoUnlinkWarningHint

        if (!authenticatorManager.isInitialized) {
            return
        }

        val config = authenticatorManager.config ?: return

// Update UI to match SDK config passed upon initialization

        mPinCode!!.isChecked = config.isMethodAllowedSimple(SecurityService.FACTOR_PIN)
        mCircleCode!!.isChecked = config.isMethodAllowedSimple(SecurityService.FACTOR_CIRCLE)
        mWearables!!.isChecked = config.isMethodAllowedSimple(SecurityService.FACTOR_PROXIMITY)
        mLocations!!.isChecked = config.isMethodAllowedSimple(SecurityService.FACTOR_GEOFENCING)
        mFingerprintScan!!.isChecked = config.isMethodAllowedSimple(SecurityService.FACTOR_FINGERPRINT)
        mDelayWearables!!.setText(Integer.toString(config.activationDelayProximitySeconds()))
        mDelayLocations!!.setText(Integer.toString(config.activationDelayGeofencingSeconds()))
        mAuthFailure!!.setText(Integer.toString(config.thresholdAuthFailure()))
        mAutoUnlink!!.setText(Integer.toString(config.thresholdAutoUnlink()))
        mAutoUnlinkWarning!!.setText(Integer.toString(config.thresholdAutoUnlinkWarning()))
        mAllowChangesWhenUnlinked!!.isChecked = config.areSecurityChangesAllowedWhenUnlinked()

        try {

            val subdomain = getString(R.string.lk_auth_sdk_oendsub)
            val domain = getString(R.string.lk_auth_sdk_oenddom)
            val endpoint = getString(
                    R.string.demo_activity_list_feature_config_endpoint_format, subdomain, domain)
            mEndpoint!!.text = endpoint
            mEndpoint!!.visibility = View.VISIBLE
        } catch (e: Resources.NotFoundException) {
            // Do nothing
        }

    }

    private fun onReinitSdk() {

        val key = KeyStorage.getInstance(this)!!.key

        if (key == null || key.trim { it <= ' ' }.isEmpty()) {

            showError("Key cannot be null or empty.")
            return
        }

        val allowPinCode = mPinCode!!.isChecked
        val allowCircleCode = mCircleCode!!.isChecked
        val allowWearables = mWearables!!.isChecked
        val allowLocations = mLocations!!.isChecked
        val allowFingerprintScan = mFingerprintScan!!.isChecked
        val delayWearablesStr = mDelayWearables!!.text.toString()

        if (delayWearablesStr.trim { it <= ' ' }.isEmpty()) {
            showError("Activation delay for Wearables cannot be empty")
            return
        }

        val delayWearables: Int
        try {
            delayWearables = Integer.parseInt(delayWearablesStr)
        } catch (e: NumberFormatException) {
            showError("Activation delay for Wearables must be a number")
            return
        }

        val delayLocationsStr = mDelayLocations!!.text.toString()

        if (delayLocationsStr.trim { it <= ' ' }.isEmpty()) {
            showError("Activation delay for Locations cannot be empty")
            return
        }

        val delayLocations: Int
        try {
            delayLocations = Integer.parseInt(delayLocationsStr)
        } catch (e: NumberFormatException) {
            showError("Activation delay for Locations must be a number")
            return
        }

        val authFailureStr = mAuthFailure!!.text.toString()

        if (authFailureStr.trim { it <= ' ' }.isEmpty()) {
            showError("Auth Failure threshold cannot be empty")
            return
        }

        val authFailure: Int
        try {
            authFailure = Integer.parseInt(authFailureStr)
        } catch (e: NumberFormatException) {
            showError("Auth Failure threshold must be a number")
            return
        }

        val autoUnlinkStr = mAutoUnlink!!.text.toString()

        if (autoUnlinkStr.trim { it <= ' ' }.isEmpty()) {
            showError("Auto Unlink threshold cannot be empty")
            return
        }

        val autoUnlink: Int
        try {
            autoUnlink = Integer.parseInt(autoUnlinkStr)
        } catch (e: NumberFormatException) {
            showError("Auto Unlink threshold must be a number")
            return
        }

        val autoUnlinkWarningStr = mAutoUnlinkWarning!!.text.toString()

        if (autoUnlinkWarningStr.trim { it <= ' ' }.isEmpty()) {
            showError("Auto Unlink warning threshold cannot be empty")
            return
        }

        val autoUnlinkWarning: Int
        try {
            autoUnlinkWarning = Integer.parseInt(autoUnlinkWarningStr)
        } catch (e: NumberFormatException) {
            showError("Auto Unlink warning threshold must be a number")
            return
        }

        val allowChangesWhenUnlinked = mAllowChangesWhenUnlinked!!.isChecked

        // Let Authenticator SDK validate threshold arguments

        val config: AuthenticatorConfig

        try {

            config = AuthenticatorConfig.Builder(this, key)
                    .allowAuthMethod(SecurityService.FACTOR_PIN, allowPinCode)
                    .allowAuthMethod(SecurityService.FACTOR_CIRCLE, allowCircleCode)
                    .allowAuthMethod(SecurityService.FACTOR_PROXIMITY, allowWearables)
                    .allowAuthMethod(SecurityService.FACTOR_GEOFENCING, allowLocations)
                    .allowAuthMethod(SecurityService.FACTOR_FINGERPRINT, allowFingerprintScan)
                    .activationDelayProximity(delayWearables)
                    .activationDelayGeofencing(delayLocations)
                    .thresholdAuthFailure(authFailure)
                    .thresholdAutoUnlink(autoUnlink, autoUnlinkWarning)
                    .allowSecurityChangesWhenUnlinked(allowChangesWhenUnlinked)
                    .build()
        } catch (e: IllegalArgumentException) {

            showError(e.message!!)
            return
        }

        // Force-unlink to clear any data before re-initializing
        authenticatorManager.unlinkCurrentDevice(object : SimpleOperationCallback<Any> {

            override fun onResult(successful: Boolean, error: BaseError?, extra: Any?) {

                authenticatorManager.initialize(config)
                finish()
            }
        })
    }

    private fun showError(message: String) {

        AlertDialog.Builder(this)
                .setTitle("Could not reinitialize SDK")
                .setMessage(message)
                .setNeutralButton(R.string.demo_generic_ok, null)
                .create()
                .show()
    }
}

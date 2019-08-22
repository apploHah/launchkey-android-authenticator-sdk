package com.launchkey.android.authenticator.demo.ui.activity

import android.support.v7.app.AppCompatActivity
import com.launchkey.android.authenticator.demo.ui.BaseDemoView
import com.launchkey.android.authenticator.sdk.AuthenticatorManager

/**
 * Created by armando on 7/8/16.
 */
open class BaseDemoActivity : AppCompatActivity(), BaseDemoView {
    override val authenticatorManager: AuthenticatorManager = AuthenticatorManager.getInstance()
}

package com.launchkey.android.authenticator.demo.ui.fragment

import android.support.v4.app.Fragment
import com.launchkey.android.authenticator.demo.ui.BaseDemoView
import com.launchkey.android.authenticator.sdk.AuthenticatorManager

/**
 * Created by armando on 7/8/16.
 */
open class BaseDemoFragment : Fragment(), BaseDemoView {
    override val authenticatorManager: AuthenticatorManager = AuthenticatorManager.getInstance()
}
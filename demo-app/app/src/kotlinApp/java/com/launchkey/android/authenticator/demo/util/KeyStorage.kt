package com.launchkey.android.authenticator.demo.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class KeyStorage private constructor(context: Context) {

    private val mPreferences: SharedPreferences

    private var mKey: String? = null

    var key: String?
        get() = mKey
        set(key) {
            mKey = key
            save()
        }

    init {

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        load()
    }

    private fun load() {

        mKey = mPreferences.getString(PREF_KEY, null)
    }

    private fun save() {

        mPreferences.edit().putString(PREF_KEY, mKey).apply()
    }

    fun hasKey(): Boolean {

        return key != null
    }

    fun clearKey() {

        key = null
    }

    companion object {

        private val PREF_KEY = "auth-sdk-key"

        @Volatile
        private var sInstance: KeyStorage? = null

        fun getInstance(context: Context): KeyStorage? {

            if (sInstance == null) {
                synchronized(KeyStorage::class.java) {
                    if (sInstance == null) {
                        sInstance = KeyStorage(context.applicationContext)
                    }
                }
            }

            return sInstance
        }
    }
}
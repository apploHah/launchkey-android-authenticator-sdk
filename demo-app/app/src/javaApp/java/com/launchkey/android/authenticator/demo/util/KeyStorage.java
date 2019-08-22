package com.launchkey.android.authenticator.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KeyStorage {

    private static final String PREF_KEY = "auth-sdk-key";

    private static volatile KeyStorage sInstance;

    public static KeyStorage getInstance(final Context context) {

        if (sInstance == null) {
            synchronized (KeyStorage.class) {
                if (sInstance == null) {
                    sInstance = new KeyStorage(context.getApplicationContext());
                }
            }
        }

        return sInstance;
    }

    private SharedPreferences mPreferences;

    private KeyStorage(final Context context) {

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        load();
    }

    private String mKey;

    private void load() {

        mKey = mPreferences.getString(PREF_KEY, null);
    }

    private void save() {

        mPreferences.edit().putString(PREF_KEY, mKey).apply();
    }

    public boolean hasKey() {

        return getKey() != null;
    }

    public String getKey() {
        return mKey;
    }

    public void clearKey() {

        setKey(null);
    }

    public void setKey(String key) {
        mKey = key;
        save();
    }
}
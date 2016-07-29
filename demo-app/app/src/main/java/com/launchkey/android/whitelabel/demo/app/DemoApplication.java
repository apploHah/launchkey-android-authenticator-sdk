package com.launchkey.android.whitelabel.demo.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.ui.activity.ListDemoActivity;
import com.launchkey.android.whitelabel.sdk.WhiteLabelApplication;
import com.launchkey.android.whitelabel.sdk.WhiteLabelConfig;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;

public class DemoApplication extends WhiteLabelApplication {

    private static final int NOTIFICATION_ID = 100;

    private NotificationManagerCompat mNotificationManager;

    @Override
    public void onCreate() {
        init();
        prepareForRequestNotifications();
        super.onCreate();
    }

    private void init() {
        WhiteLabelConfig c = new WhiteLabelConfig.Builder(this, R.string.whitelabel_sdk_key)
                .themeColorPrimaryRes(R.color.demo_primary)
                .themeColorPrimaryTextAndIconsRes(R.color.demo_primaryTextIcons)
                .themeColorSecondaryRes(R.color.demo_accent)
                .themeColorBackgroundsRes(R.color.demo_backgrounds)
                .build();

        WhiteLabelManager.getInstance().init(c);
    }

    private void prepareForRequestNotifications() {

        BroadcastReceiver requestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                notifyOfRequest();
            }
        };

        IntentFilter requestFilter = new IntentFilter(WhiteLabelManager.ACTION_EVENT_REQUEST_INCOMING);
        LocalBroadcastManager.getInstance(this).registerReceiver(requestReceiver, requestFilter);
    }

    private void notifyOfRequest() {
        if (mNotificationManager == null) {
            mNotificationManager = NotificationManagerCompat.from(this);
        }

        Intent tapIntent = new Intent(this, ListDemoActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

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
}

package com.launchkey.android.authenticator.demo.app

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat

import com.google.firebase.messaging.RemoteMessage
import com.launchkey.android.authenticator.demo.R
import com.launchkey.android.authenticator.demo.ui.activity.ListDemoActivity

class Notifier private constructor(private val mAppContext: Context) {
    private val mNotifications: NotificationManager?

    init {
        mNotifications = mAppContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun notifyOfRequest(optionalPushNotification: RemoteMessage.Notification?) {

        val tapIntent = Intent(mAppContext, ListDemoActivity::class.java)
        tapIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        tapIntent.putExtra(ListDemoActivity.EXTRA_SHOW_REQUEST, true)

        val tapPendingIntent = PendingIntent.getActivity(
                mAppContext, 1, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = createAndGetNotificationChannel()

        // Check if a push notification has defined a notification and use its title and body instead
        var title: String? = null
        var body: String? = null

        if (optionalPushNotification != null) {

            title = optionalPushNotification.title
            body = optionalPushNotification.body
        }

        if (title == null) {

            title = mAppContext.getString(R.string.app_name)
        }

        if (body == null) {

            body = mAppContext.getString(R.string.demo_notification_message)
        }

        val notification = NotificationCompat.Builder(mAppContext, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .setColor(ContextCompat.getColor(mAppContext, R.color.demo_primary))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(tapPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .build()

        mNotifications!!.notify(NOTIFICATION_ID, notification)
    }

    private fun createAndGetNotificationChannel(): String {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val id = mAppContext.getString(R.string.demo_notification_channel_authrequests_id)
            val name = mAppContext.getString(R.string.demo_notification_channel_authrequests_name)
            val desc = mAppContext.getString(R.string.demo_notification_channel_authrequests_desc)

            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = desc
            channel.setShowBadge(true)
            channel.enableLights(true)
            channel.enableVibration(true)

            mNotifications!!.createNotificationChannel(channel)

            return id
        }

        return "none"
    }

    fun cancelRequestNotification() {

        mNotifications?.cancel(NOTIFICATION_ID)
    }

    companion object {

        private val NOTIFICATION_ID = 100

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: Notifier? = null

        fun getInstance(context: Context): Notifier? {

            if (sInstance == null) {
                synchronized(Notifier::class.java) {
                    if (sInstance == null) {
                        sInstance = Notifier(context.applicationContext)
                    }
                }
            }

            return sInstance
        }
    }
}

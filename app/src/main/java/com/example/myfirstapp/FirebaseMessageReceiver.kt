package com.example.myfirstapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import com.example.myfirstapp.mainViews.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
/**
 * Firebase messaging service receiver for handling incoming messages and displaying notifications.
 */
class FirebaseMessageReceiver : FirebaseMessagingService() {
    /**
     * Called when a new token is generated for the device. This method is called when a new registration token
     * is issued to the device by Firebase Cloud Messaging (FCM).
     *
     * @param token The new FCM token generated for the device.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
    /**
     * Called when a message is received from Firebase Cloud Messaging.
     * If the message contains a notification, it will display a notification to the user.
     *
     * @param remoteMessage The message received from FCM.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.getNotification() != null) {
            showNotification(
                remoteMessage.getNotification()!!.getTitle()!!,
                remoteMessage.getNotification()!!.getBody()!!
            )
        }
    }
    /**
     * Creates a custom layout for the notification to be shown to the user.
     *
     * @param title The title to be displayed in the notification.
     * @param message The message to be displayed in the notification.
     * @return A RemoteViews object containing the custom layout.
     */
    @SuppressLint("RemoteViewLayout")
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews(
            ApplicationProvider.getApplicationContext<Context>().packageName,
            R.layout.notification
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        return remoteViews
    }
    /**
     * Displays the notification with the provided title and message.
     *
     * @param title The title to be displayed in the notification.
     * @param message The message to be displayed in the notification.
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val channelId = "notification_channel"
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.caremate_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, "Web App", importance)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.notify(0, builder.build())
    }
}
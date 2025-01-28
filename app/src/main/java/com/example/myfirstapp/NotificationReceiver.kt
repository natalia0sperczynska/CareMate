package com.example.myfirstapp
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences = context.getSharedPreferences("CareMatePreferences", Context.MODE_PRIVATE)
        val notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", true)

        if (!notificationsEnabled) return
        val message = intent.getStringExtra("notificationMessage") ?: "Reminder!"

        Log.d("NotificationReceiver", "Received notification message: $message")
        Log.d("NotificationReceiver", "Received broadcast with message: ${intent.getStringExtra("EXTRA_MESSAGE")}")

        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, "notification_channel")
            .setSmallIcon(R.drawable.profile_pic)
            .setContentTitle("Care Mate Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )  {
                Log.d("NotificationReceiver", "Displaying notification: $message")
                notify(System.currentTimeMillis().toInt(), notification)
                sharedPreferences.edit().putString("lastNotificationMessage", message).apply()
            } else {
                Log.d("NotificationReceiver", "POST_NOTIFICATIONS permission not granted")
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        val name = "Care Mate Notifications"
        val descriptionText = "Notifications for reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("notification_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

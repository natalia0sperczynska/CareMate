package com.example.myfirstapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myfirstapp.R
import java.time.LocalDateTime
import java.util.Calendar
/**
 * BroadcastReceiver for handling alarm events.
 * This class is triggered when an alarm is received and schedules the next alarm.
 */
class AlarmReceiver : BroadcastReceiver() {
    /**
     * Called when the alarm is triggered.
     * Displays a notification and schedules the next alarm for the following day.
     *
     * @param context The application context.
     * @param intent The received intent containing alarm details.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val alarmMessage = intent.getStringExtra("EXTRA_MESSAGE") ?: "Time to take your meds!"
        Log.d("AlarmReceiver", "Alarm received: $alarmMessage")

        showNotification(context, "Care Mate", alarmMessage)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)

        val alarmItem = AlarmItem(
            time = LocalDateTime.now()
                .withHour(calendar.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendar.get(Calendar.MINUTE))
                .withSecond(0),
            message = alarmMessage
        )
        AlarmSchedulerImpl(context).schedule(alarmItem)
    }
    /**
     * Displays a notification when the alarm is triggered.
     *
     * @param context The application context.
     * @param title The title of the notification.
     * @param message The message content of the notification.
     */
    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, SetNotificationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "notification_channel")
            .setSmallIcon(R.drawable.caremate_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "notification_channel",
                "Care Mate Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())
    }
}

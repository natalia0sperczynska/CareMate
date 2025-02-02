package com.example.myfirstapp.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

/**
 * Implementation of the AlarmScheduler interface.
 * Provides methods for scheduling and canceling alarms.
 */
class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val scheduledAlarms = mutableListOf<AlarmItem>()

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(alarmItem: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            Log.d("AlarmSchedulerImpl", "BUBA")
            putExtra("EXTRA_MESSAGE", alarmItem.message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmItem.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarmItem.time.hour)
            set(Calendar.MINUTE, alarmItem.time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        Log.d("AlarmSchedulerImpl", "lala: ${calendar.time}")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        scheduledAlarms.add(alarmItem)
    }


    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun clearAll() {
        scheduledAlarms.forEach { cancel(it) }
        scheduledAlarms.clear()
    }
}

package com.example.myfirstapp.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.mainViews.MainUser
import java.time.LocalDateTime
import java.util.Calendar

/**
 * Activity for setting notification alarms.
 * Allows users to set morning and evening alarms for medication reminders.
 */
class SetNotificationActivity : AppCompatActivity() {
    private lateinit var morningTimeButton :Button
    private lateinit var eveningTimeButton: Button
    private lateinit var goBack: Button
    private lateinit var clear: Button
    private lateinit var alarmScheduler: AlarmScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestExactAlarmPermission()

        setContentView(R.layout.activity_set_notification)

        alarmScheduler = AlarmSchedulerImpl(this)

        setup()

        morningTimeButton.setOnClickListener {
            showTimePicker { hour, minute ->
                Log.d("SetNotificationActivity", "Morning alarm selected: $hour:$minute")
                val alarmItem = AlarmItem(
                    time = LocalDateTime.now()
                        .withHour(hour)
                        .withMinute(minute)
                        .withSecond(0),
                    message = "Time to take your morning medication!"
                )
                alarmScheduler.schedule(alarmItem)
                Log.d("SetNotificationActivity", "Scheduled morning alarm: $alarmItem")
                Toast.makeText(this, "Morning alarm set!", Toast.LENGTH_SHORT).show()
            }
        }

        eveningTimeButton.setOnClickListener {
            showTimePicker { hour, minute ->
                val alarmItem = AlarmItem(
                    time = LocalDateTime.now()
                        .withHour(hour)
                        .withMinute(minute)
                        .withSecond(0),
                    message = "Time to take your evening medication!"
                )
                alarmScheduler.schedule(alarmItem)
                Toast.makeText(this, "Evening alarm set!", Toast.LENGTH_SHORT).show()
            }
        }
        /**
         * Clears all scheduled alarms.
         */
        clear.setOnClickListener {
            clearAlarms()
        }

        goBack.setOnClickListener {
            val intent = Intent(this, MainUser::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

        val alarmManager = getSystemService(AlarmManager::class.java)
        if (!alarmManager.canScheduleExactAlarms()) {
            startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:$packageName")
            })
        }
    }
    /**
     * Displays a time picker dialog for the user to select a time.
     *
     * @param onTimeSelected Callback to return the selected hour and minute.
     */

    private fun showTimePicker(onTimeSelected: (hour: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            onTimeSelected(selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }
    /**
     * Initializes UI elements.
     */
    private fun setup(){
        morningTimeButton = findViewById(R.id.morningTimeButton)
        eveningTimeButton = findViewById(R.id.eveningTimeButton)
        goBack = findViewById(R.id.gobackbutton1)
        clear = findViewById(R.id.clear)
    }

    private fun clearAlarms() {
        alarmScheduler.clearAll()
        Toast.makeText(this, "All alarms cleared!", Toast.LENGTH_SHORT).show()
    }

}
package com.example.myfirstapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.mainViews.MainUser
import java.time.LocalDateTime
import java.util.Calendar


class SetNotificationActivity : AppCompatActivity() {
    private lateinit var alarmScheduler: AlarmScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_notification)

        alarmScheduler = AlarmSchedulerImpl(this)


        val morningTimeButton: Button = findViewById(R.id.morningTimeButton)
        val eveningTimeButton: Button = findViewById(R.id.eveningTimeButton)
        val goBack: Button = findViewById(R.id.gobackbutton1)
        val clear : Button = findViewById(R.id.clear)

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
        goBack.setOnClickListener {
            val intent = Intent(this, MainUser::class.java)
            startActivity(intent)
        }
//        clear.setOnClickListener {
//            alarmScheduler.cancel(alarmItem)
//        }

    }
    private fun showTimePicker(onTimeSelected: (hour: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            onTimeSelected(selectedHour, selectedMinute)
        }, hour, minute, true).show()
    }

}
package com.example.myfirstapp.notifications

import java.time.LocalDateTime
/**
 * Data class representing an alarm item.
 *
 * @property time The time when the alarm should trigger.
 * @property message The message to be displayed.
 */
data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)

package com.example.myfirstapp.notifications

/**
 * Interface defining methods for scheduling and canceling alarms.
 */
interface AlarmScheduler {
    /**
     * Schedules an alarm.
     *
     * @param alarmItem The alarm item to be scheduled.
     */
    fun schedule(alarmItem: AlarmItem)
    /**
     * Cancels an existing alarm.
     *
     * @param alarmItem The alarm item to be canceled.
     */
    fun cancel(alarmItem: AlarmItem)
    /**
     * Clear all existing alarms.
     *
     */
    fun clearAll()



}
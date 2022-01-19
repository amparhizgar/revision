package com.amirhparhizgar.revision.service.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import com.amirhparhizgar.revision.service.Util
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.setting.SettingStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

open class SchedulerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val settingStore: SettingStore,
    private val repository: Repository
) : Scheduler {

    /**
     * schedules next day that review must happen. cancels previous alarm if there is no task
     */
    override suspend fun scheduleOrCancel() {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val scheduleTime = getScheduleTime()
        if (scheduleTime != null && settingStore.todoReminding.value()) {
            AlarmManagerCompat.setExact(
                alarmManager, AlarmManager.RTC_WAKEUP, scheduleTime, getPendingIntent()
            )
            Log.d(
                Util.TAG,
                "SchedulerImpl->schedule: scheduled for" +
                        " ${(scheduleTime - System.currentTimeMillis()) / 3600000} hours: $scheduleTime"
            )
        } else { // no task to schedule or reminding is disabled. canceling the alarm if any.
            alarmManager.cancel(getPendingIntent())
            Log.d(Util.TAG, "SchedulerImpl->schedule: alarm cancelled")
        }
    }

    open suspend fun getScheduleTime(): Long? {
        val theTask = repository.getTheTaskAfterToday() ?: return null
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = theTask.nextRepetitionMillis
        calendar.set(Calendar.HOUR_OF_DAY, settingStore.remindingHour.value())
        calendar.set(Calendar.MINUTE, 0)
        return calendar.timeInMillis
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, Receiver::class.java).apply {
            action = Receiver.SEND_DAILY_NOTIFICATION_ACTION
        }
        return PendingIntent.getBroadcast(context, 1, intent, Util.pendingIntentUpdateCurrentFlag)
    }
}
package com.amirhparhizgar.revision.service.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.amirhparhizgar.revision.service.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

open class SchedulerImpl @Inject constructor(
    @ApplicationContext
    val context: Context
) : Scheduler {

    override fun schedule() {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.let {
            AlarmManagerCompat.setExact(
                alarmManager, AlarmManager.RTC_WAKEUP, getScheduleTime(), getPendingIntent()
            )
        }
    }

    open fun getScheduleTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis += 24 * 60 * 60 * 1000
        calendar.set(Calendar.HOUR_OF_DAY, 9) // todo get setting from settingStore
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
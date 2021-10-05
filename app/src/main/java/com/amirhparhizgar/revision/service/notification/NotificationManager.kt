package com.amirhparhizgar.revision.service.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.service.Util
import com.amirhparhizgar.revision.service.Util.TAG
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.ui.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository,
    private val channelCreator: ChannelCreator
) {
    suspend fun sendTodayNotification() {
        channelCreator.create()
        dailyReminderNotification()?.let { notification ->
            with(NotificationManagerCompat.from(context)) {
                Log.d(TAG, "NotificationManager: sendTodayNotification: $notification")
                notify(dailyReminderId(), notification)
            }
        }
    }

    /**
     * creates daily notification
     * @return null if there is no task for today. else, a notification ready to send.
     */
    suspend fun dailyReminderNotification(): Notification? {
        val content = notificationContent() ?: return null
        return NotificationCompat.Builder(context, ChannelCreator.DAILY_REMINDING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.new_reviews_due_today))
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(pendingIntentForMainActivity())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .build()
    }

    private fun dailyReminderId(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    private fun pendingIntentForMainActivity(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 1, intent, Util.pendingIntentUpdateCurrentFlag)
    }

    private suspend fun notificationContent(): String? {
        val tasks = repository.getTodoTasks().first()
        return when (tasks.size) {
            0 -> null
            1 -> context.getString(R.string.one_task_due, tasks[0].name)
            2 -> context.getString(R.string.two_task_due, tasks[0].name, tasks[1].name)
            else -> context.getString(R.string.more_task_due, tasks[0].name, tasks[1].name)
        }
    }
}
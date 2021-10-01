package com.amirhparhizgar.revision.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.amirhparhizgar.revision.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ChannelCreator @Inject constructor(
    @ApplicationContext
    val context: Context
) {

    fun create() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.daily_reminder_channel_name)
            val descriptionText = context.getString(R.string.daily_reminder_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(DAILY_REMINDING_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val DAILY_REMINDING_CHANNEL_ID = "DAILY_REMINDING"
    }
}
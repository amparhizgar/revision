package com.amirhparhizgar.revision.service.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.amirhparhizgar.revision.service.Util.TAG
import com.amirhparhizgar.revision.service.notification.NotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class Receiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: action: ${intent.action}")

        when (intent.action) {
            SEND_DAILY_NOTIFICATION_ACTION -> {
                runBlocking {
                    notificationManager.sendTodayNotification()
                }
            }
        }
    }

    companion object {
        const val SEND_DAILY_NOTIFICATION_ACTION = "SEND_DAILY_NOTIFICATION"
    }
}
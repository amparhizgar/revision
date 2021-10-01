package com.amirhparhizgar.revision.service

import android.app.PendingIntent
import android.os.Build
import java.util.*

object Util {
    fun uuid(): Int {
        val idOne: UUID = UUID.randomUUID()
        var str = "" + idOne
        val uid = str.hashCode()
        val filterStr = "" + uid
        str = filterStr.replace("-".toRegex(), "")
        return str.toInt()
    }

    val pendingIntentUpdateCurrentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    const val TAG = "Revision"
}
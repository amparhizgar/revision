package com.amirhparhizgar.revision.service.scheduler

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SchedulerAndroidTestDouble @Inject constructor(
    @ApplicationContext
    context: Context
) : SchedulerImpl(context) {
    override fun getScheduleTime(): Long {
        return System.currentTimeMillis() + 2 * 1000
    }
}
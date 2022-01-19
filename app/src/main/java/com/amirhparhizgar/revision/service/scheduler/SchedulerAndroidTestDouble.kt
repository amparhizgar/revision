package com.amirhparhizgar.revision.service.scheduler

import android.content.Context
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.setting.SettingStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SchedulerAndroidTestDouble @Inject constructor(
    @ApplicationContext
    context: Context, settingStore: SettingStore, repository: Repository
) : SchedulerImpl(context, settingStore, repository) {
    override suspend fun getScheduleTime(): Long {
        return System.currentTimeMillis() + 2 * 1000
    }
}
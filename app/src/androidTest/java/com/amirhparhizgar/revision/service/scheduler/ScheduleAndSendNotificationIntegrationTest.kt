package com.amirhparhizgar.revision.service.scheduler

import android.content.Context
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.notification.NotificationManager
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ScheduleAndSendNotificationIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: Repository

    @Inject
    @ApplicationContext
    lateinit var appContext: Context

    @Inject
    lateinit var notificationManager: NotificationManager

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun schedule() =
        runBlocking {
            repository.saveTask(
                Task(
                    id = 77,
                    name = "android course",
                    project = "project",
                    nextRepetitionMillis = System.currentTimeMillis() - 5000
                )
            )

            assertThat(notificationManager.dailyReminderNotification()).isNotNull()

            SchedulerAndroidTestDouble(appContext).schedule()

            delay(15 * 1000L)// after this delay application will terminate and notification get cancelled

            repository.deleteTask(77)
        }

}
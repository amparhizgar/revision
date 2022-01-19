package com.amirhparhizgar.revision.service.scheduler

interface Scheduler {
    suspend fun scheduleOrCancel()
}
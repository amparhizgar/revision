package com.amirhparhizgar.revision.model

import com.amirhparhizgar.revision.service.Util

data class Task(
    val id: Int = Util.uuid(),
    val name: String,
    val project: String,
    val reviewTimes: Int = 0,
    /**
     * last review time and date.
     * in format of milliseconds since epoch
     */
    val lastReview: Long = System.currentTimeMillis(),
)

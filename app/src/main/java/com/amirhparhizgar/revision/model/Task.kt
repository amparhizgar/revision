package com.amirhparhizgar.revision.model

data class Task(
    val id: Int,
    val name: String,
    val project: String,
    val reviewTimes: Int,
    /**
     * last review time and date.
     * in format of seconds since epoch
     */
    val lastReview: Long,
)

package com.amirhparhizgar.revision.model

data class Task(
    val id: Int,
    val name: String,
    val project: String,
    val nextRepetitionMillis: Long = System.currentTimeMillis(),
    val repetitions: Int = 0,
    val easinessFactor: Float = 2.5f,
    val interval: Int = 1
) {
    fun withUpdatedRepetitionProperties(
        newRepetitions: Int,
        newEasinessFactor: Float,
        newNextRepetitionMillis: Long,
        newInterval: Int
    ) = copy(
        repetitions = newRepetitions,
        easinessFactor = newEasinessFactor,
        nextRepetitionMillis = newNextRepetitionMillis,
        interval = newInterval
    )
}

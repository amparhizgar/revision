package com.amirhparhizgar.revision.model

import java.util.*

data class Card(
    val frontSide: String,
    val backSide: String,
    val nextRepetition: Calendar = Calendar.getInstance(),
    val repetitions: Int = 0,
    val easinessFactor: Float = 2.5.toFloat(),
    val interval: Int = 1
) {
    fun withUpdatedRepetitionProperties(
        newRepetitions: Int,
        newEasinessFactor: Float,
        newNextRepetitionDate: Calendar,
        newInterval: Int
    ) = copy(
        repetitions = newRepetitions,
        easinessFactor = newEasinessFactor,
        nextRepetition = newNextRepetitionDate,
        interval = newInterval
    )
}
package com.amirhparhizgar.revision.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amirhparhizgar.revision.service.Util

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: Int = Util.uuid(),
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

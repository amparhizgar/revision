package com.amirhparhizgar.revision.model

import java.util.logging.Logger
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * algorithm of Spaced Repetition.
 * [details](https://www.supermemo.com/en/archives1990-2015/english/ol/sm2)
 *
 * note: this class is copied from [here](https://blog.mestwin.net/spaced-repetition-algorithm-implementation-in-kotlin)
 * and has been modified
 */
object SpacedRepetition {

    object Quality {
        const val Relearn = 1
        const val Hard = 3
        const val Good = 4
        const val Easy = 5

        val list = listOf(Relearn, Hard, Good, Easy)
    }

    fun calculateRepetition(task: Task, quality: Int): Task {
        validateQualityFactorInput(quality)

        val easiness = calculateEasinessFactor(task.easinessFactor, quality)
        val repetitions = calculateRepetitions(quality, task.repetitions)
        val interval = calculateInterval(repetitions, task.interval, easiness)

        val cardAfterRepetition = task.withUpdatedRepetitionProperties(
            newRepetitions = repetitions,
            newEasinessFactor = easiness,
            newNextRepetitionMillis = calculateNextPracticeDate(interval),
            newInterval = interval
        )
        log.info(cardAfterRepetition.toString())
        return cardAfterRepetition
    }

    private fun validateQualityFactorInput(quality: Int) {
        log.info("Input quality: $quality")
        if (quality < 0 || quality > 5) {
            throw IllegalArgumentException("Provided quality value is invalid ($quality)")
        }
    }

    private fun calculateEasinessFactor(easiness: Float, quality: Int) =
        max(1.3, easiness + 0.1 - (5.0 - quality) * (0.08 + (5.0 - quality) * 0.02)).toFloat()


    private fun calculateRepetitions(quality: Int, cardRepetitions: Int) = if (quality < 3) {
        0
    } else {
        cardRepetitions + 1
    }

    private fun calculateInterval(repetitions: Int, cardInterval: Int, easiness: Float) = when {
        repetitions <= 1 -> 1
        repetitions == 2 -> 6
        else -> (cardInterval * easiness).roundToInt()
    }

    private fun calculateNextPracticeDate(interval: Int): Long {
        val now = System.currentTimeMillis()
        return now + dayInMs * interval
    }


    private const val dayInMs: Long = 24 * 60 * 60 * 1000L
    private val log: Logger = Logger.getLogger(SpacedRepetition::class.java.name)


}
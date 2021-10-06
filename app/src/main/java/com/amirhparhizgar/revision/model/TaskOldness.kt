package com.amirhparhizgar.revision.model

import androidx.compose.ui.graphics.Color

sealed class TaskOldness(val range: IntRange, val index: Int) {
    object Unseen : TaskOldness(IntRange(0, 1), 0)
    object Learning : TaskOldness(IntRange(2, 5), 1)
    object Young : TaskOldness(IntRange(6, 14), 2)
    object Mature : TaskOldness(IntRange(15, Int.MAX_VALUE), 3)

    fun color(): Color {
        return colorList[index]
    }

    companion object {
        val list = listOf(Unseen, Learning, Young, Mature)
        val colorList =
            listOf(Color(0xFF777777), Color(0xFFfa7339), Color(0xFF10ad44), Color(0xFF0A67E0))

        fun fromInterval(interval: Int): TaskOldness {
            list.forEach { taskOldness ->
                if (interval in taskOldness.range)
                    return taskOldness
            }
            throw IllegalArgumentException("interval must be grater that or equal to zero")
        }
    }
}
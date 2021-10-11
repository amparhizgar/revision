package com.amirhparhizgar.revision.model

import com.amirhparhizgar.revision.R

sealed class TaskOldness(val range: IntRange, val index: Int) {
    object Unseen : TaskOldness(IntRange(0, 1), 0)
    object Learning : TaskOldness(IntRange(2, 5), 1)
    object Young : TaskOldness(IntRange(6, 14), 2)
    object Mature : TaskOldness(IntRange(15, Int.MAX_VALUE), 3)

    fun colorResId(): Int {
        return colorList[index]
    }

    companion object {
        val list = listOf(Unseen, Learning, Young, Mature)
        val colorList =
            listOf(
                R.color.unseen_color,
                R.color.learning_color,
                R.color.young_color,
                R.color.mature_color
            )

        fun fromInterval(interval: Int): TaskOldness {
            list.forEach { taskOldness ->
                if (interval in taskOldness.range)
                    return taskOldness
            }
            throw IllegalArgumentException("interval must be grater that or equal to zero")
        }
    }
}
package com.amirhparhizgar.revision.model

sealed class TaskOldness(val range: Pair<Int, Int>) {
    object Unseen : TaskOldness(Pair(0, 1))
    object Learning : TaskOldness(Pair(1, 5))
    object Young : TaskOldness(Pair(5, 14))
    object Mature : TaskOldness(Pair(14, Int.MAX_VALUE))
}
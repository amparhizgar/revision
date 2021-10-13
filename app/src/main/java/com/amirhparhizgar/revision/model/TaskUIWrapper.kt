package com.amirhparhizgar.revision.model

data class TaskUIWrapper(
    val task: Task,
    val due: String,
    val isPassed: Boolean,
    val oldness: TaskOldness,
    var isSelected: Boolean = false
) {
    fun select(): TaskUIWrapper = apply { isSelected = true }
    fun unselect(): TaskUIWrapper = apply { isSelected = false }
}

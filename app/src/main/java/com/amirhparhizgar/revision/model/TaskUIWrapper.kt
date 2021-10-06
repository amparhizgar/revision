package com.amirhparhizgar.revision.model

data class TaskUIWrapper(
    val task: Task,
    val due: String,
    val isPassed: Boolean,
    val oldness: TaskOldness
)

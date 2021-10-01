package com.amirhparhizgar.revision.service.data_source

import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTask(id: Int): Task
    fun getTasksForToday(): Flow<List<Task>>
    suspend fun saveTask(task: Task)
    fun getOldnessCount(oldness: TaskOldness): Int
    fun getProjectsStartWith(start: String): List<String>
}
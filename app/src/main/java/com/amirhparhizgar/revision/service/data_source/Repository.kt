package com.amirhparhizgar.revision.service.data_source

import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getTask(id: Int): Task
    fun getTodoTasks(): Flow<List<Task>>
    fun deleteTask(id: Int)
    suspend fun saveTask(task: Task)
    fun getOldnessCount(oldness: TaskOldness): Flow<Int>
    fun getProjectsStartWith(start: String): List<String>
    fun getProjects(): Flow<List<String>>
}
package com.amirhparhizgar.revision.service.data_source

import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(taskDatabase: TaskDatabase) : Repository {

    private val dao = taskDatabase.taskDao

    override fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    override fun getTask(id: Int): Task = dao.getTask(id)

    override suspend fun saveTask(task: Task) = dao.saveTask(task)

    override fun getOldnessCount(oldness: TaskOldness): Int {
        return dao.getIntervalCount(oldness.range.first, oldness.range.second)
    }

    override fun getProjectsStartWith(start: String): List<String> {
        return dao.getDistinctProjectsLike("$start%")
    }
}
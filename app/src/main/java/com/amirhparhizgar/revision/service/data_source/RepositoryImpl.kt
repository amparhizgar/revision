package com.amirhparhizgar.revision.service.data_source

import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject


class RepositoryImpl @Inject constructor(taskDatabase: TaskDatabase) : Repository {

    private val dao = taskDatabase.taskDao

    override fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    override fun getTask(id: Int): Task = dao.getTask(id)

    override fun getTasksForToday(): Flow<List<Task>> {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }
        val startingMillis = calendar.timeInMillis
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 24)
            set(Calendar.MINUTE, 0)
        }
        val endingMillis = calendar.timeInMillis
        return dao.getTaskBetweenTimes(startingMillis, endingMillis)
    }

    override fun deleteTask(id: Int) {
        dao.deleteTask(id)
    }

    override suspend fun saveTask(task: Task) = dao.saveTask(task)

    override fun getOldnessCount(oldness: TaskOldness): Int {
        return dao.getIntervalCount(oldness.range.first, oldness.range.second)
    }

    override fun getProjectsStartWith(start: String): List<String> {
        return dao.getDistinctProjectsLike("$start%")
    }
}
package com.amirhparhizgar.revision.service.data_source

import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject


class RepositoryImpl @Inject constructor(taskDatabase: TaskDatabase) : Repository {

    private val dao = taskDatabase.taskDao

    override fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    override suspend fun getTask(id: Int): Task = dao.getTask(id)

    /**
     * returns a flow of all tasks that is due for today and before
     */
    override fun getTodoTasks(): Flow<List<Task>> {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 24)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val endingMillis = calendar.timeInMillis
        return dao.getTaskBetweenTimes(1, endingMillis)
    }

    /**
     * returns a flow of all tasks that is due for today and before
     */
    override suspend fun getTheTaskAfterToday(): Task? {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 24)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val startMillis = calendar.timeInMillis
        return dao.getTheTaskAfterToday(startMillis, Long.MAX_VALUE)
    }

    override fun deleteTask(id: Int) {
        dao.deleteTask(id)
    }

    override suspend fun saveTask(task: Task) = dao.saveTask(task)

    override fun getOldnessCount(oldness: TaskOldness): Flow<Int> {
        return dao.getIntervalCountInclusive(oldness.range.first, oldness.range.last)
    }

    override fun getProjectsStartWith(start: String): List<String> {
        return dao.getDistinctProjectsLike("$start%")
    }

    override fun getProjects(): Flow<List<String>> {
        return dao.getProjects().map { list -> list.filter { project -> project != "" } }
    }
}
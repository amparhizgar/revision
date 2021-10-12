package com.amirhparhizgar.revision.viewmodel

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.SpacedRepetition
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import com.amirhparhizgar.revision.model.TaskUIWrapper
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.human_readable_date.HumanReadableDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class BaseTaskViewModel constructor(
    protected val repository: Repository,
    private val humanReadableDate: HumanReadableDate
) : ViewModel() {

    val scrollState = ScrollState(0)

    protected fun getTasks(onlyTodo: Boolean): Flow<List<TaskUIWrapper>> {
        val taskListFlow = if (onlyTodo)
            repository.getTodoTasks()
        else
            repository.getAllTasks()
        return taskListFlow.map { list -> list.map(taskToTaskUIWrapper(onlyTodo)) }
    }

    private fun taskToTaskUIWrapper(onlyTodo: Boolean): (Task) -> TaskUIWrapper = {
        val converter = if (onlyTodo)
            humanReadableDate::convertToRelative
        else
            humanReadableDate::convertToAbsolute
        TaskUIWrapper(
            it,
            converter(it.nextRepetitionMillis),
            humanReadableDate.diffDays(it.nextRepetitionMillis) < 0,
            TaskOldness.fromInterval(it.interval)
        )
    }

    fun getNextReviewDates(task: Task): List<String> {
        val list = mutableListOf<String>()
        SpacedRepetition.Quality.list.forEach { quality ->
            val nextReview = SpacedRepetition.calculateRepetition(task, quality)
            list.add(humanReadableDate.convertToRelative(nextReview.nextRepetitionMillis))
        }

        return list
    }

    fun onDone(taskId: Int, quality: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldTask = repository.getTask(taskId)
            val newTask = SpacedRepetition.calculateRepetition(oldTask, quality)
            repository.saveTask(newTask)
        }
    }

    fun delete(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(taskId)
        }
    }
}
package com.amirhparhizgar.revision.viewmodel

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.SpacedRepetition
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import com.amirhparhizgar.revision.model.TaskUIWrapper
import com.amirhparhizgar.revision.service.Util
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.human_readable_date.HumanReadableDate
import com.amirhparhizgar.revision.service.scheduler.Scheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseTaskViewModel constructor(
    protected val repository: Repository,
    private val humanReadableDate: HumanReadableDate,
    private val scheduler: Scheduler
) : ViewModel() {
    abstract val tasks: StateFlow<List<TaskUIWrapper>>

    val selections = MutableStateFlow(setOf<Int>())

    val events = MutableSharedFlow<TodoScreenEvents>()

    val selectionCount: StateFlow<Int> =
        selections.map { it.size }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        scheduleInScope()
    }

    fun unselectAll() {
        selections.value = setOf()
    }

    fun onTaskClicked(index: Int) {
        if (selectionCount.value > 0)
            onTaskLongClicked(index)
        else {
            viewModelScope.launch {
                events.emit(TodoScreenEvents.GoSingleScreen(tasks.value[index].task))
            }
        }
    }

    fun onTaskLongClicked(index: Int) {
        selections.value = selections.value.toMutableSet().apply {
            if (this.contains(index))
                remove(index)
            else
                add(index)
        }
    }

    fun deleteSelection() {
        val selectionCache = selections.value
        val tasksCache = tasks.value
        selections.value = emptySet()
        viewModelScope.launch(Dispatchers.IO) {
            selectionCache.forEach { delete(tasksCache[it].task.id) }
            schedule()
        }
    }

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
            schedule()
        }
    }

    fun delete(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(taskId)
            schedule()
        }
    }

    fun Flow<List<TaskUIWrapper>>.combineWithSelections(): Flow<List<TaskUIWrapper>> =
        this.combine(selections) { taskWrappers, selections ->
            val a = taskWrappers.toList().apply {
                forEachIndexed { index, wrapper ->
                    wrapper.isSelected = index in selections
                }
            }
            Log.d(Util.TAG, "TodoViewModel->a: $a")
            return@combine a
        }


    private fun scheduleInScope() {
        viewModelScope.launch {
            schedule()
        }
    }

    private suspend fun schedule() {
        scheduler.scheduleOrCancel()
    }
}
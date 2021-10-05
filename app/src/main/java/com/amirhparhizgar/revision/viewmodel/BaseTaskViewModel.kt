package com.amirhparhizgar.revision.viewmodel

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.service.data_source.Repository
import kotlinx.coroutines.flow.Flow

open class BaseTaskViewModel constructor(protected val repository: Repository) : ViewModel() {

    val scrollState = ScrollState(0)

    fun getTasks(onlyTodo: Boolean): Flow<List<Task>> {
        return if (onlyTodo)
            repository.getTodoTasks()
        else
            repository.getAllTasks()
    }
}
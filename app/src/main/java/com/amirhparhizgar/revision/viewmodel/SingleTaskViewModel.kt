package com.amirhparhizgar.revision.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.service.data_source.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleTaskViewModel @Inject constructor(
    val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var id: Int = savedStateHandle.get<Int>("id") ?: -1

    private val _taskName = mutableStateOf<String>("")
    val taskName: State<String> = _taskName

    fun setTaskName(taskName: String) {
        _taskName.value = taskName
    }

    private val _project = mutableStateOf<String>("")
    val project: State<String> = _project

    fun setProject(project: String) {
        _project.value = project
    }

    init {
        if (id != -1)
            viewModelScope.launch {
                val taskFromRepo = repository.getTask(id)
                _taskName.value = taskFromRepo.name
                _project.value = taskFromRepo.project
            }
    }

    fun save() {
        viewModelScope.launch {
            if (id != -1) {
                val task = repository.getTask(id)
                repository.saveTask(task.copy(name = taskName.value, project = project.value))
            } else
                repository.saveTask(Task(name = taskName.value, project = project.value))
        }
    }
}
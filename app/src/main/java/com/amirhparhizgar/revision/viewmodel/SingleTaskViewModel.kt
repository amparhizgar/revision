package com.amirhparhizgar.revision.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.service.data_source.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
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

    private val _project = MutableStateFlow<TextFieldValue>(TextFieldValue())
    val project: StateFlow<TextFieldValue> = _project

    fun setProject(project: TextFieldValue) {
        _project.value = project
        projectDropDownExpandedPreference.value = true
    }

    init {
        if (id != -1)
            viewModelScope.launch {
                val taskFromRepo = repository.getTask(id)
                _taskName.value = taskFromRepo.name
                _project.value =
                    TextFieldValue(taskFromRepo.project, TextRange(taskFromRepo.project.length))
            }
    }

    fun save() {
        viewModelScope.launch {
            if (id != -1) {
                val task = repository.getTask(id)
                repository.saveTask(task.copy(name = taskName.value, project = project.value.text))
            } else
                repository.saveTask(Task(name = taskName.value, project = project.value.text))
        }
    }

    val projectListFiltered: StateFlow<List<String>> = project.map {
        if (it.text.length > 1) {
            val fromRepo = repository.getProjectsStartWith(it.text)
            if (fromRepo.size == 1 && fromRepo[0] == project.value.text)
                emptyList()
            else
                fromRepo
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope.plus(Dispatchers.IO), SharingStarted.Eagerly, listOf())

    private val projectDropDownExpandedPreference: MutableStateFlow<Boolean> =
        MutableStateFlow(true)

    val projectDropDownExpanded: StateFlow<Boolean> =
        projectListFiltered.map { it.isNotEmpty() }
            .combine(projectDropDownExpandedPreference) { a, b ->
                a && b
            }
            .stateIn(viewModelScope.plus(Dispatchers.IO), SharingStarted.Eagerly, false)


    fun onDropdownDismissRequest() {
        projectDropDownExpandedPreference.value = false
    }
}
package com.amirhparhizgar.revision.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.service.data_source.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    repository: Repository,
    savedStateHandle: SavedStateHandle
) : BaseTaskViewModel(repository) {
    val tasks: Flow<List<Task>>
        get() {
            return getTasks(true)
        }
}
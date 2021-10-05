package com.amirhparhizgar.revision.viewmodel

import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.service.data_source.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AllTasksViewModel @Inject constructor(repository: Repository) :
    BaseTaskViewModel(repository) {

    val tasks: Flow<List<Task>>
        get() {
            return getTasks(false)
        }
}
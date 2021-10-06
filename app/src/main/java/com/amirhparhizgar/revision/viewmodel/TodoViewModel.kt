package com.amirhparhizgar.revision.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.amirhparhizgar.revision.model.TaskUIWrapper
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.human_readable_date.HumanReadableDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    repository: Repository,
    humanReadableDate: HumanReadableDate,
    savedStateHandle: SavedStateHandle
) : BaseTaskViewModel(repository, humanReadableDate) {
    val tasks: Flow<List<TaskUIWrapper>>
        get() {
            return getTasks(true)
        }
}
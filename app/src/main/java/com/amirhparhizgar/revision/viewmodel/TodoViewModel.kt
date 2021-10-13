package com.amirhparhizgar.revision.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskUIWrapper
import com.amirhparhizgar.revision.service.Util
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.human_readable_date.HumanReadableDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    repository: Repository,
    humanReadableDate: HumanReadableDate,
    savedStateHandle: SavedStateHandle
) : BaseTaskViewModel(repository, humanReadableDate) {



    override val tasks: StateFlow<List<TaskUIWrapper>> = getTasks(true)
        .combine(selections) { taskWrappers, selections ->
            val a = taskWrappers.toList().apply {
                forEachIndexed { index, wrapper ->
                    wrapper.isSelected = index in selections
                }
            }
            Log.d(Util.TAG, "TodoViewModel->a: $a")
            return@combine a
        }.combineWithSelections()
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())

}

sealed class TodoScreenEvents {
    data class GoSingleScreen(val task: Task?) : TodoScreenEvents()
}
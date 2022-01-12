package com.amirhparhizgar.revision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.BarChart
import com.amirhparhizgar.revision.model.TaskOldness
import com.amirhparhizgar.revision.service.data_source.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    private val barFlow = combine(
        repository.getOldnessCount(TaskOldness.UNSEEN),
        repository.getOldnessCount(TaskOldness.LEARNING),
        repository.getOldnessCount(TaskOldness.YOUNG),
        repository.getOldnessCount(TaskOldness.MATURE)
    ) { unseen, learning, young, mature ->
        BarChart.oldnessBarChart(unseen, learning, young, mature)
    }

    val barchart: StateFlow<BarChart> = barFlow.stateIn(
        viewModelScope + Dispatchers.IO,
        SharingStarted.Eagerly,
        BarChart.emptyOldnessChart
    )
}
package com.amirhparhizgar.revision.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.DropDownWrapper
import com.amirhparhizgar.revision.model.TaskUIWrapper
import com.amirhparhizgar.revision.service.Util.TAG
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.human_readable_date.HumanReadableDate
import com.amirhparhizgar.revision.service.scheduler.Scheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AllTasksViewModel @Inject constructor(
    repository: Repository,
    humanReadableDate: HumanReadableDate,
    scheduler: Scheduler
) :
    BaseTaskViewModel(repository, humanReadableDate, scheduler) {
    private val selectedProjectIndex = MutableStateFlow(0)

    fun setSelectedProject(selectedProject: Int) {
        this.selectedProjectIndex.value = selectedProject
    }

    override val tasks: StateFlow<List<TaskUIWrapper>> = getTasks(false)
        .combine(this.selectedProjectIndex)
        { list, selectedProjectIndex ->
            val projectsCache = projectsWithWrapper.value.list
            Log.d(TAG, "AllTasksViewModel->tasks(get): projectsCache = $projectsCache")
            list.filter {
                return@filter when {
                    projectsCache.isEmpty() -> true
                    selectedProjectIndex == 0 -> true
                    selectedProjectIndex > projectsCache.lastIndex -> true
                    else -> {
                        projectsCache[selectedProjectIndex] == it.task.project
                    }
                }
            }
        }
        .combineWithSelections()
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())


    val projectsWithWrapper: StateFlow<DropDownWrapper<String>> =
        repository.getProjects().combine(this.selectedProjectIndex)
        { fromRepo, selectedIndex ->
            DropDownWrapper(
                list = mutableListOf(
                    "All !"
                ).apply { addAll(fromRepo) },
                selected = selectedIndex
            )
        }
            .onEach {
                if (this.selectedProjectIndex.value > it.list.lastIndex)
                    setSelectedProject(0)
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialValue = DropDownWrapper(list = listOf("All "), selected = 0)
            )

}
package com.amirhparhizgar.revision.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhparhizgar.revision.model.ThemeMode
import com.amirhparhizgar.revision.service.scheduler.Scheduler
import com.amirhparhizgar.revision.service.setting.SettingStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext context: Context, val settingStore: SettingStore, val scheduler: Scheduler
) : ViewModel() {
    val reminderChecked: StateFlow<Boolean> = settingStore.todoReminding.flow.stateHere(false)

    fun setReminderChecked(reminderChecked: Boolean) = viewModelScope.launch {
        settingStore.todoReminding.set(reminderChecked)
        scheduler.scheduleOrCancel()
    }

    fun toggleReminderChecked() = viewModelScope.launch {
        settingStore.todoReminding.set(reminderChecked.value.not())
        scheduler.scheduleOrCancel()
    }

    val remindingHour: StateFlow<Int> = settingStore.remindingHour.flow.stateHere(8)

    fun setRemindingHour(remindingHour: Int) = viewModelScope.launch {
        settingStore.remindingHour.set(remindingHour)
        hideSelectTimeDialog()
        scheduler.scheduleOrCancel()
    }

    private val _selectTimeDialogVisible = mutableStateOf(false)
    val selectTimeDialogVisible: State<Boolean> = _selectTimeDialogVisible

    fun showSelectTimeDialog() {
        _selectTimeDialogVisible.value = true
    }

    fun hideSelectTimeDialog() {
        _selectTimeDialogVisible.value = false
    }

    val themeSelection: StateFlow<ThemeMode> =
        settingStore.themeSetting.flow.map { ThemeMode.fromId(it) }.stateHere(ThemeMode.AUTOMATIC)

    fun setThemeMode(themeSelection: ThemeMode) = viewModelScope.launch {
        settingStore.themeSetting.set(themeSelection.id)
    }

    private fun <T> Flow<T>.stateHere(initialValue: T): StateFlow<T> {
        return stateIn(
            viewModelScope,
            SharingStarted.Eagerly, initialValue
        )
    }
}
package com.amirhparhizgar.revision.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.amirhparhizgar.revision.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val _reminderChecked = mutableStateOf(false) // todo load form storage in init{}
    val reminderChecked: State<Boolean> = _reminderChecked

    fun setReminderChecked(reminderChecked: Boolean) {
        _reminderChecked.value = reminderChecked
    }

    fun toggleReminderChecked() {
        _reminderChecked.value = !_reminderChecked.value
    }

    private val _remindingHour = mutableStateOf(8) // todo load form storage in init{}
    val remindingHour: State<Int> = _remindingHour

    fun setRemindingHour(remindingHour: Int) {
        _remindingHour.value = remindingHour
        hideSelectTimeDialog()
    }

    private val _selectTimeDialogVisible = mutableStateOf(false)
    val selectTimeDialogVisible: State<Boolean> = _selectTimeDialogVisible

    fun showSelectTimeDialog() {
        _selectTimeDialogVisible.value = true
    }

    fun hideSelectTimeDialog() {
        _selectTimeDialogVisible.value = false
    }

    private val _themeMode = mutableStateOf(ThemeMode.AUTOMATIC) // todo load form storage in init{}
    val themeSelection: State<ThemeMode> = _themeMode

    fun setThemeMode(themeSelection: ThemeMode) {
        _themeMode.value = themeSelection
    }
}
package com.amirhparhizgar.revision.ui.screen

import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Today
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.BottomNavTab
import com.amirhparhizgar.revision.model.Screen
import com.amirhparhizgar.revision.ui.common.MyAppIcons

val todoScreen = BottomNavTab(R.string.todo_tab, MyAppIcons.Today, "todo")
val taskScreen = BottomNavTab(R.string.tasks_tab, MyAppIcons.List, "tasks")
val profileScreen = BottomNavTab(R.string.profile_tab, MyAppIcons.AccountCircle, "profile")
val bottomTabList = listOf(
    todoScreen,
    taskScreen,
    profileScreen,
)
val settingScreen = Screen("setting")
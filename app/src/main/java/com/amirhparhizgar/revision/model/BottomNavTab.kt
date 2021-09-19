package com.amirhparhizgar.revision.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavTab(
    @StringRes
    val title: Int,
    val icon: ImageVector,
    override val destination: String
) : Screen(destination)


package com.amirhparhizgar.revision.ui.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow

data class SheetPack @ExperimentalMaterialApi constructor(
    val flow: MutableStateFlow<@Composable ColumnScope.() -> Unit>,
    val state: ModalBottomSheetState,
)
package com.amirhparhizgar.revision.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.SpacedRepetition
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.NewTaskButton
import com.amirhparhizgar.revision.ui.common.ReviewBottomSheet
import com.amirhparhizgar.revision.ui.common.TaskList
import com.amirhparhizgar.revision.viewmodel.TodoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoScreen(
    goSingleScreen: (Task?) -> Unit,
    todoViewModel: TodoViewModel = hiltViewModel()
) {
    val sheetOpenedFor = remember { mutableStateOf<Task?>(null) }
    val bottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val taskListState = todoViewModel.tasks.collectAsState(initial = emptyList())

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            ReviewBottomSheet(
                onSelect = { qualityIndex ->
                    if (sheetOpenedFor != null) {
                        todoViewModel.onDone(
                            sheetOpenedFor.value!!.id,
                            SpacedRepetition.Quality.list[qualityIndex]
                        )
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    }
                },
                nextReviews = sheetOpenedFor.value?.let { todoViewModel.getNextReviewDates(it) }
                    ?: listOf("", "", "", "")
            )
        }
    ) {
        Column {
            TopAppBar(title = {
                Text(stringResource(id = R.string.to_do), style = MaterialTheme.typography.h5)
            },
                actions = {
                    NewTaskButton {
                        goSingleScreen(null)
                    }
                }
            )

            TaskList(
                taskListState,
                goSingleScreen,
                onDismiss = { todoViewModel.delete(it.id) },
                scope,
                sheetOpenedFor,
                bottomSheetState
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun TodoScreenPreview() {
    TodoScreen(goSingleScreen = {})
}
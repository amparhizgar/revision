package com.amirhparhizgar.revision.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.SpacedRepetition
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.ContextualTaskAppBar
import com.amirhparhizgar.revision.ui.common.NewTaskButton
import com.amirhparhizgar.revision.ui.common.ReviewBottomSheet
import com.amirhparhizgar.revision.ui.common.TaskList
import com.amirhparhizgar.revision.viewmodel.TodoScreenEvents
import com.amirhparhizgar.revision.viewmodel.TodoViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoScreen(
    goSingleScreen: (Task?) -> Unit,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val sheetOpenedFor = remember { mutableStateOf<Task?>(null) }
    val bottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val taskListState = viewModel.tasks.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = true) {
        scope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is TodoScreenEvents.GoSingleScreen ->
                        goSingleScreen(event.task)
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            ReviewBottomSheet(
                onSelect = { qualityIndex ->
                    if (sheetOpenedFor != null) {
                        viewModel.onDone(
                            sheetOpenedFor.value!!.id,
                            SpacedRepetition.Quality.list[qualityIndex]
                        )
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    }
                },
                nextReviews = sheetOpenedFor.value?.let { viewModel.getNextReviewDates(it) }
                    ?: listOf("", "", "", "")
            )
        }
    ) {

        val selectionCount = viewModel.selectionCount.collectAsState()
        Column {
            if (selectionCount.value != 0)
                ContextualTaskAppBar(
                    onUnselectAll = viewModel::unselectAll,
                    onDelete = viewModel::deleteSelection, selectionCount
                )
            else
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.to_do),
                            style = MaterialTheme.typography.h5
                        )
                    },
                    actions = {
                        NewTaskButton {
                            goSingleScreen(null)
                        }
                    }
                )

            TaskList(
                taskListState = taskListState,
                onDismiss = { viewModel.delete(it.id) },
                onTaskClick = viewModel::onTaskClicked,
                onTaskLongClick = viewModel::onTaskLongClicked,
                scope = scope,
                sheetOpenedFor = sheetOpenedFor,
                bottomSheetState = bottomSheetState
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun TodoScreenPreview() {
    TodoScreen(goSingleScreen = {})
}
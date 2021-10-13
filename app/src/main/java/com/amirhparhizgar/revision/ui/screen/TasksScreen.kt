package com.amirhparhizgar.revision.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.SpacedRepetition
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.*
import com.amirhparhizgar.revision.viewmodel.AllTasksViewModel
import com.amirhparhizgar.revision.viewmodel.TodoScreenEvents
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


val mockTasks = listOf(
    Task(1, "item 1", "", 2, 35444545),
    Task(2, "item 2", "", 5, 35444565)
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TasksScreen(
    goSingleScreen: (Task?) -> Unit,
    viewModel: AllTasksViewModel = hiltViewModel()
) {
    val sheetOpenedFor = remember { mutableStateOf<Task?>(null) }
    val bottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val taskListState = viewModel.tasks.collectAsState(initial = listOf())
    val scope = rememberCoroutineScope()

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
                    viewModel.onDone(
                        sheetOpenedFor.value!!.id,
                        SpacedRepetition.Quality.list[qualityIndex]
                    )
                    scope.launch {
                        bottomSheetState.hide()
                    }
                },
                nextReviews = sheetOpenedFor.value?.let { viewModel.getNextReviewDates(it) }
                    ?: listOf("", "", "", "")
            )
        }
    ) {
        val projectListState =
            viewModel.projectsWithWrapper.collectAsState()

        val selectionCount = viewModel.selectionCount.collectAsState()
        Column {
            if (selectionCount.value != 0)
                ContextualTaskAppBar(
                    onUnselectAll = viewModel::unselectAll,
                    onDelete = viewModel::deleteSelection, selectionCount
                )
            else
                TopAppBar(title = {
                    TaskFilterDropDown(
                        items = projectListState.value.list.toMutableList().apply {
                            set( // localize "All" item
                                index = 0,
                                stringResource(id = R.string.all_projects)
                            )
                        },
                        selected = projectListState.value.selected,
                        onSelect = { index ->
                            viewModel.setSelectedProject(index)
                        })
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

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    TasksScreen(goSingleScreen = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewDropDown() {
    TaskFilterDropDown(items = listOf("All projects", "Zen"), selected = 0, onSelect = { })
}

@Composable
fun TaskFilterDropDown(items: List<String>, selected: Int, onSelect: (index: Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.h5
    val trailingIconColor =
        LocalContentColor.current.copy(alpha = TextFieldDefaults.IconOpacity)

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .clickable(onClick = { expanded = true })
            .padding(8.dp)
    ) {
        Row(
            Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                items[selected],
                modifier = Modifier
                    .wrapContentSize(),
                style = textStyle
            )
            Icon(
                imageVector = MyAppIcons.ArrowDropDown,
                contentDescription = "Change",
                tint = trailingIconColor,
                modifier = Modifier
                    .padding(top = 4.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onSelect(index)
                }) {
                    Text(text = item)
                }
            }
        }
    }
}
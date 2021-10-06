package com.amirhparhizgar.revision.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amirhparhizgar.revision.model.SpacedRepetition
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.MyAppIcons
import com.amirhparhizgar.revision.ui.common.NewTaskButton
import com.amirhparhizgar.revision.ui.common.ReviewBottomSheet
import com.amirhparhizgar.revision.ui.common.TaskRow
import com.amirhparhizgar.revision.viewmodel.AllTasksViewModel
import kotlinx.coroutines.launch


val mockTasks = listOf(
    Task(1, "item 1", "", 2, 35444545),
    Task(2, "item 2", "", 5, 35444565)
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TasksScreen(
    goSingleScreen: (Task?) -> Unit,
    tasksViewModel: AllTasksViewModel = hiltViewModel()
) {
    var sheetOpenedFor by remember { mutableStateOf<Task?>(null) }
    val bottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val taskListState = tasksViewModel.tasks.collectAsState(initial = listOf())
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            ReviewBottomSheet(
                onSelect = { qualityIndex ->
                    if (sheetOpenedFor != null) {
                        tasksViewModel.onDone(
                            sheetOpenedFor!!.id,
                            SpacedRepetition.Quality.list[qualityIndex]
                        )
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    }
                },
                nextReviews = sheetOpenedFor?.let { tasksViewModel.getNextReviewDates(it) }
                    ?: listOf("", "", "", "")
            )
        }
    ) {
        Column {
            TopAppBar(title = {
                TaskFilterDropDown(
                    items = listOf("all", "something else"),
                    onSelected = { _, _ -> })
            },
                actions = {
                    NewTaskButton {
                        goSingleScreen(null)
                    }
                }
            )

            LazyColumn {
                itemsIndexed(taskListState.value) { _, item: Task ->
                    TaskRow(
                        modifier = Modifier.clickable { goSingleScreen(item) },
                        title = item.name, onDone = {
                            scope.launch {
                                sheetOpenedFor = item
                                bottomSheetState.show()
                            }
                        },
                        checked = sheetOpenedFor == item && (bottomSheetState.targetValue != ModalBottomSheetValue.Hidden)
                    )
                }
            }
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
    TaskFilterDropDown(items = listOf("All projects", "Zen"), onSelected = { _, _ -> })
}

@Composable
fun TaskFilterDropDown(items: List<String>, onSelected: (index: Int, project: String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
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
                items[selectedIndex],
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
                    selectedIndex = index
                    expanded = false
                    onSelected(index, item)
                }) {
                    Text(text = item)
                }
            }
        }
    }
}
package com.amirhparhizgar.revision.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.MyAppIcons
import com.amirhparhizgar.revision.ui.common.NewTaskButton
import com.amirhparhizgar.revision.ui.common.ReviewBottomSheet
import com.amirhparhizgar.revision.ui.common.TaskRow
import kotlinx.coroutines.launch

val mockTasks = listOf(
    Task(1, "item 1", "", 2, 35444545),
    Task(2, "item 2", "", 5, 35444565)
)

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun TasksScreen() {
    var sheetOpenedFor by remember { mutableStateOf(-1) }
    val onSheetClosed = { sheetOpenedFor = -1 }
    val bottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, confirmStateChange = {
            if (it == ModalBottomSheetValue.Hidden)
                onSheetClosed()
            return@rememberModalBottomSheetState true
        })
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            val context = LocalContext.current
            ReviewBottomSheet(onSelect = {
                Toast.makeText(context, "hello form $sheetOpenedFor", Toast.LENGTH_SHORT).show()
                scope.launch {
                    onSheetClosed()
                    bottomSheetState.hide()
                }
            }, nextReviews = listOf("test", "test", "todo", "!!!!!"))
        }
    ) {
        Column {
            TopAppBar(title = {
                TaskFilterDropDown(
                    items = listOf("all", "something else"),
                    onSelected = { _, _ -> })
            },
                actions = {
                    NewTaskButton()
                }
            )

            LazyColumn {
                itemsIndexed(mockTasks) { index, item: Task ->
                    TaskRow(
                        title = item.name, onDone = {
                            scope.launch {
                                sheetOpenedFor = index
                                bottomSheetState.show()
                            }
                        },
                        checked = sheetOpenedFor == index
                    )
                }
            }
        }
    }
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
package com.amirhparhizgar.revision.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.NewTaskButton
import com.amirhparhizgar.revision.ui.common.ReviewBottomSheet
import com.amirhparhizgar.revision.ui.common.TaskRow
import com.amirhparhizgar.revision.viewmodel.AllTasksViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoScreen(
    goSingleScreen: (Task?) -> Unit,
    tasksViewModel: AllTasksViewModel = hiltViewModel()
) {
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
                Text(stringResource(id = R.string.to_do), style = MaterialTheme.typography.h5)
            },
                actions = {
                    NewTaskButton {
                        goSingleScreen(null)
                    }
                }
            )
            LazyColumn {
                itemsIndexed(mockTasks) { index, item: Task ->
                    TaskRow(
                        modifier = Modifier.clickable { goSingleScreen(item) },
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun TodoScreenPreview() {
    TodoScreen(goSingleScreen = {})
}
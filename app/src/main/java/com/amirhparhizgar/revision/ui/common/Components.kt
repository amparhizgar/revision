package com.amirhparhizgar.revision.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.BottomNavTab
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import com.amirhparhizgar.revision.model.TaskUIWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val MyAppIcons = Icons.Rounded

@Preview(name = "Task", showBackground = true)
@Composable
fun TaskRow(
    modifier: Modifier = Modifier,
    oldness: TaskOldness = TaskOldness.Unseen,
    isPassed: Boolean = false,
    date: String = "Sun",
    selected: Boolean = false,
    title: String = "title",
    onDone: () -> Unit = {},
    checked: Boolean = false
) {
    val background = if (selected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Row(
        modifier
            .background(color = background)
            .padding(8.dp)
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskProgressIndicator(oldness)
        Text(
            text = title, modifier = Modifier.weight(1F),
            style = MaterialTheme.typography.h5
        )
        val dateTextColor =
            if (isPassed) colorResource(id = R.color.red_text) else LocalContentColor.current
        Text(
            date,
            style = MaterialTheme.typography.h5.copy(color = dateTextColor)
        )
        CircleCheckbox(
            onClick = onDone,
            checked = checked
        )
    }
}

@Composable
private fun TaskProgressIndicator(oldness: TaskOldness) {
    Box(
        modifier = Modifier
            .background(colorResource(id = oldness.colorResId()))
            .width(4.dp)
            .fillMaxHeight()
    )
}

@Composable
private fun CircleCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onClick: () -> Unit
) {
    var cornerColor = Color.Gray
    val fillColorUnchecked = MaterialTheme.colors.surface
    val fillColorChecked = Color.Green
    val fillColor: Color
    if (checked) {
        cornerColor = fillColorChecked
        fillColor = fillColorChecked
    } else {
        fillColor = fillColorUnchecked
    }
    val padding = 4.dp
    Box(
        modifier = modifier
            .size(24.dp + padding * 2)
            .padding(padding)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(cornerColor)
            .padding(2.dp)
            .clip(CircleShape)
            .background(fillColor),
        contentAlignment = Alignment.Center
    ) {
        if (checked)
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = MyAppIcons.Check,
                tint = MaterialTheme.colors.surface,
                contentDescription = ""
            )
    }

}

@Preview
@Composable
fun NewTaskButton(onClick: () -> Unit = {}) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
    ) {
        Icon(
            imageVector = MyAppIcons.Add,
            contentDescription = stringResource(id = R.string.plus_icon)
        )
        Text(stringResource(id = R.string.new_tast))
    }
}

@Composable
fun TaskBottomNav(list: List<BottomNavTab>, selected: Int, onSelect: (Int) -> Unit) {
    BottomNavigation {
        list.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(id = item.title)) },
                selected = index == selected,
                onClick = { onSelect(index) },
                alwaysShowLabel = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomSheet() {
    ReviewBottomSheet(onSelect = {}, listOf("1 day", "2 day", "3 day", "4 day"))
}

@Composable
fun ReviewBottomSheet(onSelect: (Int) -> Unit, nextReviews: List<String>) {
    if (nextReviews.size != 4) throw IllegalArgumentException("nextReviews must have 4 items")
    Column {
        0.let {
            QualityRow(
                modifier = Modifier.clickable { onSelect(it) },
                icon = MyAppIcons.SentimentVeryDissatisfied,
                tint = Color(0xFFB31111),
                quality = R.string.relearn,
                nextReview = nextReviews[it]
            )
        }
        1.let {
            QualityRow(
                modifier = Modifier.clickable { onSelect(it) },
                icon = MyAppIcons.SentimentDissatisfied,
                tint = Color(0xFFFF5722),
                quality = R.string.hard,
                nextReview = nextReviews[it]
            )
        }
        2.let {
            QualityRow(
                modifier = Modifier.clickable { onSelect(it) },
                icon = MyAppIcons.SentimentSatisfied,
                tint = Color(0xFF4CAF50),
                quality = R.string.good,
                nextReview = nextReviews[it]
            )
        }
        3.let {
            QualityRow(
                modifier = Modifier.clickable { onSelect(it) },
                icon = MyAppIcons.SentimentVerySatisfied,
                tint = Color(0xFF1F39CA),
                quality = R.string.easy,
                nextReview = nextReviews[it]
            )
        }

    }
}

@Composable
private fun QualityRow(
    modifier: Modifier,
    icon: ImageVector,
    tint: Color,
    @StringRes quality: Int,
    nextReview: String
) {
    Row(modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, tint = tint, contentDescription = null)
        Text(
            text = stringResource(id = quality),
            Modifier
                .weight(1F)
                .padding(start = 16.dp)
        )
        Text(text = nextReview)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun TaskList(
    taskListState: State<List<TaskUIWrapper>>,
    onTaskClick: (index: Int) -> Unit,
    onTaskLongClick: (index: Int) -> Unit,
    onDismiss: (Task) -> Unit,
    scope: CoroutineScope,
    sheetOpenedFor: MutableState<Task?>,
    bottomSheetState: ModalBottomSheetState
) {
    LazyColumn {
        itemsIndexed(
            items = taskListState.value,
            key = { _, item: TaskUIWrapper -> item.task.id }) { index: Int, wrapper: TaskUIWrapper ->
            val task = wrapper.task
            AnimatedSwipeDismiss(
                item = wrapper,
                background = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 5.dp, vertical = 0.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "",
                            tint = LocalContentColor.current
                        )
                    }
                },
                onDismiss = { onDismiss(wrapper.task) },
                content = {
                    TaskRow(
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
//                            .clickable { goSingleScreen(task) }
//                            .able
//                            .selectable(selected = true, onClick = {onSelection(index)}, role = Role.Checkbox)
                            .combinedClickable(onClick = { onTaskClick(index) },
                                onLongClick = { onTaskLongClick(index) }),
                        selected = wrapper.isSelected,
                        title = wrapper.task.name, onDone = {
                            scope.launch {
                                sheetOpenedFor.value = task
                                bottomSheetState.show()
                            }
                        }, date = wrapper.due,
                        isPassed = wrapper.isPassed,
                        oldness = wrapper.oldness,
                        checked = sheetOpenedFor.value == task && (bottomSheetState.targetValue != ModalBottomSheetValue.Hidden)
                    )
                }
            )

        }
    }
}


@Composable
fun ContextualTaskAppBar(
    onUnselectAll: () -> Unit,
    onDelete: () -> Unit,
    selectionCount: State<Int>
) {
    TopAppBar(navigationIcon = {
        IconButton(onClick = onUnselectAll) {
            Icon(
                imageVector = MyAppIcons.Cancel,
                contentDescription = "cancel selection"
            )
        }
    }, title = { Text("${selectionCount.value} selected") },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = MyAppIcons.Delete,
                    contentDescription = "delete selected"
                )
            }
        })
}

// not used now
fun clipIcon(imageVector: ImageVector): ImageVector {
    return ImageVector.Builder(
        tintColor = Color.Green,
        defaultHeight = imageVector.defaultHeight,
        defaultWidth = imageVector.defaultWidth,
        viewportHeight = imageVector.viewportHeight,
        viewportWidth = imageVector.viewportWidth
    ).addPath(imageVector.root.clipPathData).build()
}
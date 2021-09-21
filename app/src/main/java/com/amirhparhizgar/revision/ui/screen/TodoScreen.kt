package com.amirhparhizgar.revision.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.NewTaskButton
import com.amirhparhizgar.revision.ui.common.TaskRow

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun TodoScreen() {
    Column {
        TopAppBar(title = {
            Text(stringResource(id = R.string.to_do), style = MaterialTheme.typography.h5)
        },
            actions = {
                NewTaskButton()
            }
        )
        LazyColumn {
            items(mockTasks) { item: Task ->
                TaskRow(title = item.name)
            }
        }
    }
}
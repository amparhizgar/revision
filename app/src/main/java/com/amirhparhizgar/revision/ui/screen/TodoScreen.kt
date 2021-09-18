package com.amirhparhizgar.revision.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.NewTaskButton
import com.amirhparhizgar.revision.ui.common.TaskRow

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun TodoScreen() {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.to_do), style = MaterialTheme.typography.h5)
            NewTaskButton()
        }
        LazyColumn {
            items(mockTasks) { item: Task ->
                TaskRow(title = item.name)
            }
        }
    }
}
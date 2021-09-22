package com.amirhparhizgar.revision.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Category
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.MyAppIcons

@Composable
fun AddTaskScreen(onBack: () -> Unit, onSave: (Task) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = MyAppIcons.ArrowBack, contentDescription = "navigate back")
                }
            },
            title = { Text(text = stringResource(id = R.string.add_task_title)) })
    }) {
        Column(
            Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val paddingStart = 45.dp
            val (name, onSetName) = remember { mutableStateOf("") }
            val (project, onSetProject) = remember { mutableStateOf("") }
            StandardTextField(
                modifier = Modifier.padding(start = paddingStart), text = name,
                onSetText = onSetName, label = R.string.task_name
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.width(paddingStart),
                    imageVector = MyAppIcons.Category,
                    contentDescription = "project icon"
                )
                StandardTextField(Modifier, project, onSetProject, R.string.project)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = { onSave(Task(name = name, project = project)) }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }

}

@Composable
private fun StandardTextField(
    modifier: Modifier,
    text: String,
    onSetText: (String) -> Unit,
    @StringRes label: Int
) {
    TextField(modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = onSetText,
        colors = TextFieldDefaults.outlinedTextFieldColors(),
        label = { Text(text = stringResource(id = label)) })
}

@Preview
@Composable
private fun PreviewAddTask() {
    AddTaskScreen({}, {})
}
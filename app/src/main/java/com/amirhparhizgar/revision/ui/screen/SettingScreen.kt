package com.amirhparhizgar.revision.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.amirhparhizgar.revision.BuildConfig
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.ThemeMode
import com.amirhparhizgar.revision.ui.common.MyAppIcons

@Preview(showBackground = true)
@Composable
fun SettingScreen() {
    Column {
        TopAppBar(
            title = { Text(stringResource(id = R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = { /* todo add onClick */ }) {
                    Icon(MyAppIcons.ArrowBack, contentDescription = null)
                }
            }
        )
        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(scrollState)) {
            SettingGroup(text = stringResource(id = R.string.notifications)) {
                TitleDetailSettingItem(
                    modifier = Modifier.clickable { /* todo add onClick*/ },
                    title = stringResource(id = R.string.todo_reminding),
                    detail = stringResource(id = R.string.get_summary_tasks),
                    icon = MyAppIcons.Notifications
                )
                TitleDetailSettingItem(
                    modifier = Modifier.clickable { /* todo add onClick*/ },
                    title = stringResource(id = R.string.prefered_time),
                    detail = "todo (8 am)",
                    icon = MyAppIcons.Schedule
                )
            }
            SettingGroup(text = stringResource(id = R.string.backup)) {
                TitleDetailSettingItem(
                    icon = MyAppIcons.CloudUpload,
                    title = stringResource(id = R.string.status),
                    detail = "todo provide appropriate status"
                )
            }
            SettingGroup(text = stringResource(id = R.string.appearance)) {
                var themeDialogVisible by remember { mutableStateOf(false) }
                var themeDialogSelection by remember {
                    mutableStateOf(
                        ThemeMode.getRadioOption(
                            ThemeMode.AUTOMATIC
                        )
                    )
                }
                if (themeDialogVisible) {
                    ThemeDialog(
                        selection = themeDialogSelection,
                        onDismiss = { themeDialogVisible = false },
                        onSelected = {
                            themeDialogSelection = it
                            themeDialogVisible = false
                        })
                }
                TitleDetailSettingItem(
                    modifier = Modifier.clickable { themeDialogVisible = true },
                    icon = MyAppIcons.DarkMode,
                    title = stringResource(id = R.string.dark_mode),
                    detail = "todo provide appropriate details"
                )
            }
            SettingGroup(text = stringResource(id = R.string.about)) {
                TitleDetailSettingItem(
                    icon = MyAppIcons.Info,
                    title = stringResource(id = R.string.about),
                    detail = stringResource(id = R.string.version, BuildConfig.VERSION_NAME)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewThemeDialog() {
    ThemeDialog(
        selection = ThemeMode.getRadioOption(ThemeMode.AUTOMATIC),
        onDismiss = {},
        onSelected = {})
}

@Composable
fun ThemeDialog(
    onDismiss: () -> Unit,
    selection: ThemeMode.RadioOption,
    onSelected: (ThemeMode.RadioOption) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .padding(16.dp)
                .background(color = MaterialTheme.colors.surface),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(id = R.string.theme), modifier = Modifier.padding(16.dp))
            ThemeMode.list.forEach {
                val isItSelected = it.ThemeMode == selection.ThemeMode
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (isItSelected),
                            onClick = { onSelected(it) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (isItSelected),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = stringResource(id = it.textResId),
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

val iconSpaceWidth = 40.dp
val settingItemPadding = 8.dp

@Composable
fun SettingGroup(text: String, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Row(Modifier.padding(vertical = 8.dp)) {
            Spacer(modifier = Modifier.width(iconSpaceWidth + settingItemPadding))
            Text(
                text = text,
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.primary)
            )
        }
        this.content()
        Divider()
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    content: @Composable () -> Unit
) {
    Row(
        modifier
            .padding(settingItemPadding)
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(iconSpaceWidth)
                .fillMaxHeight()
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    tint = MaterialTheme.colors.onBackground.copy(alpha = 0.6F),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingGroup() {
    SettingGroup(text = "test") {
        TitleDetailSettingItem(title = "title here", detail = "detail here", icon = MyAppIcons.Edit)
        TitleDetailSettingItem(title = "title here 2", detail = "More detail here", icon = null)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewTitleDetailSetting() {
    TitleDetailSettingItem(title = "title here", detail = "detail here", icon = MyAppIcons.Edit)
}

@Composable
fun TitleDetailSettingItem(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    title: String,
    detail: String
) {
    SettingItem(modifier, icon = icon) {
        Column {
            Text(text = title, style = MaterialTheme.typography.subtitle1)
            Text(text = detail, style = MaterialTheme.typography.caption)
        }
    }
}
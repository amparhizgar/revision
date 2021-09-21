package com.amirhparhizgar.revision.ui.screen

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.ui.common.MyAppIcons

@Composable
@Preview
fun ProfileScreen() {
    TopAppBar(title = {
        Text(stringResource(id = R.string.profile))
    },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = MyAppIcons.Settings, contentDescription = "settings button")
            }
        }
    )
    // todo add content
}
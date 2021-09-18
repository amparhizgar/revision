package com.amirhparhizgar.revision.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amirhparhizgar.revision.R

val MyAppIcons = Icons.Rounded

@Preview(name = "Task", showBackground = true)
@Composable
fun TaskRow(
    modifier: Modifier = Modifier,
    level: Int = 1,
    date: String = "Sun",
    title: String = "title",
    onDone: () -> Unit = {},
    checked: Boolean = false
) {
    Row(
        modifier
            .padding(8.dp)
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskProgressIndicator()
        Text(
            text = title, modifier = Modifier.weight(1F),
            style = MaterialTheme.typography.h5
        )
        Text(date)
        CircleCheckbox(
            modifier = Modifier.clickable(onClick = onDone),
            checked = checked
        )
    }
}

@Composable
private fun TaskProgressIndicator() {
    Box(
        modifier = Modifier
            .background(Color.Green)
            .width(4.dp)
            .fillMaxHeight()
    )
}

@Preview(name = "Circle check box")
@Composable
private fun CircleCheckbox(modifier: Modifier = Modifier, checked: Boolean = false) {
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
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(24.dp)
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
    TextButton(onClick = onClick) {
        Icon(
            imageVector = MyAppIcons.Add,
            contentDescription = stringResource(id = R.string.plus_icon)
        )
        Text(stringResource(id = R.string.new_tast))
    }
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
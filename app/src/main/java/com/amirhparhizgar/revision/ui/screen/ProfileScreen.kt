package com.amirhparhizgar.revision.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amirhparhizgar.revision.R
import com.amirhparhizgar.revision.model.BarChart
import com.amirhparhizgar.revision.ui.common.MyAppIcons
import com.amirhparhizgar.revision.viewmodel.ProfileViewModel

@Composable
@Preview(showBackground = true)
fun ProfileScreen(goSettings: () -> Unit = {}, viewModel: ProfileViewModel = hiltViewModel()) {
    Column {
        TopAppBar(title = {
            Text(stringResource(id = R.string.profile))
        },
            actions = {
                IconButton(onClick = goSettings) {
                    Icon(imageVector = MyAppIcons.Settings, contentDescription = "settings button")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        val barchartState = viewModel.barchart.collectAsState()
        BarChart(barchartState.value)
    }
}

@Composable
fun BarChart(chart: BarChart) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(200.dp), horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        chart.barList.forEach { bar ->
            DataBar(bar)
        }
    }
}

@Composable
fun DataBar(bar: BarChart.Bar) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color = colorResource(id = bar.color))
                .width(50.dp)
                .fillMaxHeight(bar.ratio)
        )
        Text(text = bar.count.toString(), color = colorResource(id = bar.color))
        Text(text = stringResource(id = bar.label))
    }
}
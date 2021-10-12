package com.amirhparhizgar.revision.ui.common

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun <T> AnimatedSwipeDismiss(
    modifier: Modifier = Modifier,
    item: T,
    background: @Composable (dismissState: DismissState) -> Unit,
    content: @Composable (dismissState: DismissState) -> Unit,
    directions: Set<DismissDirection> = setOf(DismissDirection.EndToStart),
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(
        animationSpec = tween(
            durationMillis = 500,
        )
    ),
    onDismiss: (T) -> Unit
) {
    val dismissState = remember(item) {
        DismissState(initialValue = DismissValue.Default)
    }

    val isDismissed = dismissState.currentValue != DismissValue.Default

    LaunchedEffect(
        key1 = item,
        key2 = isDismissed
    ) {
        if (isDismissed)
            onDismiss(item)
    }

//    val animState = animateDpAsState(targetValue = 0.dp, tween(
//        durationMillis = 500,
//    ))

//    AnimatedVisibility(
//        visible = !isDismissed,
//        enter = enter,
//        exit = exit
//    ) {
    SwipeToDismiss(
        state = dismissState,
        directions = directions,
        dismissThresholds = {
            FractionalThreshold(1f)
        },
        background = { background(dismissState) },
        dismissContent = { content(dismissState) }
    )
//    }
}
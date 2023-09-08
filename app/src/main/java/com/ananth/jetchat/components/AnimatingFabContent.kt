package com.ananth.jetchat.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.util.lerp
import kotlin.math.roundToInt

@Composable
fun AnimatingFabContent(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    extended: Boolean = true
) {

    val currentState = if (extended) ExpandableFabStates.Extended else ExpandableFabStates.Collapsed
    val fabTransition = updateTransition(currentState, "fab_transition")

    val textOpacity by fabTransition.animateFloat(
        transitionSpec = {
            if (targetState == ExpandableFabStates.Collapsed) {
                tween(
                    easing = LinearEasing,
                    durationMillis = (transitionDuration / 12f * 5).roundToInt() // 5/12 frames
                )
            } else {
                tween(
                    easing = LinearEasing,
                    delayMillis = (transitionDuration / 3f).roundToInt(), // 4/12 frames
                    durationMillis = (transitionDuration / 12f * 5).roundToInt()
                )
            }
        },
        label = "fab_text_opacity"
    ) { state ->
        if (state == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }


    val fabWidthFactor by fabTransition.animateFloat(
        transitionSpec = {
            if (targetState == ExpandableFabStates.Collapsed) {
                tween(
                    easing = FastOutSlowInEasing,
                    durationMillis = transitionDuration

                )
            } else {
                tween(
                    easing = FastOutSlowInEasing,
                    durationMillis = transitionDuration

                )
            }
        },
        label = "fab_width_factor"
    ) { state ->
        if (state == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }

    //deferring reads using lambdas instead of Floats here we can improve the performance
    //preventing recomposition
    IconAndTextRow(
        icon = icon,
        text = text,
        opacityProgress = { textOpacity },
        widthProgress = { fabWidthFactor })

}

@Composable
fun IconAndTextRow(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    opacityProgress: () -> Float,
    widthProgress: () -> Float,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier,
        content = {
            icon()
            Box(modifier = Modifier.graphicsLayer { alpha = opacityProgress() }) {
                text()
            }
        }
    ) { measurables, constraints ->

        val iconPlaceable = measurables[0].measure(constraints)
        val textPlaceable = measurables[1].measure(constraints)

        val height = constraints.maxHeight

        //FAB has the aspect ratio of 1 so the initial width is the height
        val initialWidth = height.toFloat()

        //use it to get the padding
        val iconPadding = (initialWidth - iconPlaceable.width) / 2f

        //The full width will be : padding + icon + padding + text + padding

        val expandedWidth = iconPlaceable.width + textPlaceable.width + iconPadding * 3

        //Apply the animation factor to go from initialWidth to expandedWidth
        val width = lerp(initialWidth, expandedWidth, widthProgress())

        layout(width.roundToInt(), height) {
            iconPlaceable.place(
                iconPadding.roundToInt(),
                constraints.maxHeight / 2 - iconPlaceable.height / 2
            )
            textPlaceable.place(
                (iconPlaceable.width + iconPadding * 2).roundToInt(),
                constraints.maxHeight / 2 - textPlaceable.height / 2
            )
        }
    }
}

private enum class ExpandableFabStates {
    Collapsed, Extended
}

private const val transitionDuration = 200
package com.ananth.jetchat.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

/**
 * Applied to a Text, it sets the distance between the top and the first baseline. It
 * also makes the bottom of the element coincide with the last baseline of the text.
 *
 *     _______________
 *     |             |   ↑
 *     |             |   |  heightFromBaseline
 *     |Hello, World!|   ↓
 *     ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
 *
 * This modifier can be used to distribute multiple text elements using a certain distance between
 * baselines.
 */
class BaseLineHeightModifier(
    private val heightFromBaseline: Dp
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val textPlaceable = measurable.measure(constraints)
        val firstBaseLine = textPlaceable[FirstBaseline]
        val lastBaseLine = textPlaceable[LastBaseline]

        val height = heightFromBaseline.roundToPx() + lastBaseLine - firstBaseLine
        return layout(constraints.maxWidth, height) {
            val topY = heightFromBaseline.roundToPx() - firstBaseLine
            textPlaceable.place(0, topY)
        }
    }
}

fun Modifier.baseLineHeight(heightFromBaseline: Dp): Modifier =
    this.then(BaseLineHeightModifier(heightFromBaseline))
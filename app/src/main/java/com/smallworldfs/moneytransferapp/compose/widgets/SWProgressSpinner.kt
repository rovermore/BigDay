package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.compose.colors.primary
import com.smallworldfs.moneytransferapp.compose.colors.transparent

@Composable
fun SWProgressSpinner(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        val strokeWidth = 4f
        val initialValue = 0f
        val finalValue = 360f
        val spinSpeed = 500

        val angle by rememberInfiniteTransition(label = "rotation_animation")
            .animateFloat(
                initialValue = initialValue,
                targetValue = finalValue,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = spinSpeed,
                        easing = LinearEasing,
                    ),
                ),
                label = "rotation_animation",
            )

        val stroke = with(LocalDensity.current) {
            Stroke(
                width = strokeWidth.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = angle
                },
        ) {
            rotate(degrees = 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colorStops = listOf(0f to transparent, 1f to primary)
                            .toTypedArray(),
                    ),
                    startAngle = initialValue + strokeWidth,
                    sweepAngle = finalValue - (strokeWidth * 2),
                    useCenter = false,
                    topLeft = Offset(stroke.width / 2, stroke.width / 2),
                    size = Size(size.width - stroke.width, size.width - stroke.width),
                    style = stroke,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWProgressSpinnerPreview() {
    SWProgressSpinner(
        modifier = Modifier
            .size(60.dp),
    )
}

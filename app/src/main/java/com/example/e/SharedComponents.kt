package com.example.e

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 0f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}

fun Modifier.bottomElevation(): Modifier = this.then(
    Modifier.drawWithContent {
        val paddingPx = 8.dp.toPx()
        clipRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height + paddingPx
        ) {
            this@drawWithContent.drawContent()
        }
    }
)

@Composable
fun DotsPulsing(modifier: Modifier = Modifier) {
    val dotSize = 24.dp
    val delayUnit = 200

    @Composable
    fun Dot(
        scale: Float
    ) = Spacer(
        modifier
            .size(dotSize)
            .scale(scale)
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateScaleWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                1f at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val scale1 by animateScaleWithDelay(0)
    val scale2 by animateScaleWithDelay(delayUnit)
    val scale3 by animateScaleWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val spaceSize = 2.dp

        Dot(scale1)
        Spacer(Modifier.width(spaceSize))
        Dot(scale2)
        Spacer(Modifier.width(spaceSize))
        Dot(scale3)
    }
}

@Composable
fun ErrorCard(errorMessage: String?) {
    Crossfade(targetState = errorMessage) {
        if (it != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colors.error)
                    .padding(horizontal = 4.dp, vertical = 16.dp)
                    .animateContentSize()

            ) {
                Text(text = it, color = MaterialTheme.colors.onError)
            }
        } else {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InputWrapperCard(
    errorMessage: String?,
    loadingBar: Boolean = false,
    spacedBy: Int = 20,
    content: @Composable (() -> Unit),
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(8.dp)
            .fillMaxSize()
    ) {
        LoadingBar(loadingBar)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacedBy.dp),
        ) {
            ErrorCard(errorMessage)
            content()
        }
    }
}

@Composable
private fun LoadingBar(loadingBar: Boolean) {
    if (loadingBar) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
        ) {
            DotsPulsing()
        }
    }
}

package fr.o80.testcompose.screen.unlockcircle

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme

@Composable
fun Lock(
    modifier: Modifier,
    lockDegres: List<Float>,
    onFullyUnlocked: () -> Unit
) {
    var remainingLocks by remember { mutableStateOf(Locks(lockDegres)) }
    val keyPosition by rememberKeyPosition()

    LaunchedEffect(remainingLocks) {
        if (remainingLocks.isFullyUnlocked()) {
            onFullyUnlocked()
        }
    }

    val lockColor = LocalLockTheme.current.lockColor
    val backgroundLockColor = LocalLockTheme.current.backgroundLockColor
    val keyColor = LocalLockTheme.current.keyColor
    val strokeWidthDp = LocalLockTheme.current.strokeWidth.dp
    val lockRadiusDp = LocalLockTheme.current.lockRadius.dp
    val keyRadiusDp = LocalLockTheme.current.keyRadius.dp
    val marginDp = LocalLockTheme.current.margin.dp

    Canvas(
        modifier.pointerInput(Unit) {
            this.detectTapGestures(
                onPress = {
                    remainingLocks = remainingLocks.withUnlocked(keyPosition)
                }
            )
        }
    ) {
        val strokeWidth = strokeWidthDp.toPx()
        val smallCircleRadius = lockRadiusDp.toPx()
        val keyRadius = keyRadiusDp.toPx()
        val margin = marginDp.toPx()
        val bigCircleRadius = (size.minDimension-strokeWidth) / 2f - smallCircleRadius - margin

        drawCircle(
            Color.Red,
            radius = bigCircleRadius,
            style = Stroke(width = strokeWidth)
        )
        remainingLocks.locks.forEach { position ->
            drawLockPosition(
                position, smallCircleRadius, bigCircleRadius, strokeWidth,
                lockColor,
                backgroundLockColor
            )
        }
        drawKey({ keyPosition }, keyRadius, bigCircleRadius, keyColor)
    }
}

@Composable
fun rememberKeyPosition(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
}

private fun DrawScope.drawKey(
    keyPosition: () -> Float,
    keyRadius: Float,
    bigCircleRadius: Float,
    keyColor: Color
) {
    rotate(keyPosition()) {
        drawCircle(
            keyColor,
            radius = keyRadius,
            style = Fill,
            center = center.minus(Offset(0f, bigCircleRadius))
        )
    }
}

private fun DrawScope.drawLockPosition(
    position: Float,
    smallCircleRadius: Float,
    bigCircleRadius: Float,
    strokeWidth: Float,
    lockColor: Color,
    backgroundLockColor: Color
) {
    rotate(position) {
        drawCircle(
            backgroundLockColor,
            radius = smallCircleRadius,
            style = Fill,
            center = center.minus(Offset(0f, bigCircleRadius)),
            blendMode = BlendMode.Src
        )
        drawCircle(
            lockColor,
            radius = smallCircleRadius,
            style = Stroke(width = strokeWidth),
            center = center.minus(Offset(0f, bigCircleRadius))
        )
    }
}

@Preview(showSystemUi = false)
@Composable
fun LockPreview() {
    TestComposeCanvasTheme {
        Lock(
            Modifier.size(500.dp),
            lockDegres = listOf(0f, 90f, 135f),
            onFullyUnlocked = {}
        )
    }
}

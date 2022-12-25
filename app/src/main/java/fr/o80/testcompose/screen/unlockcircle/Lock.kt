package fr.o80.testcompose.screen.unlockcircle

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
    degres: List<Float>,
    keyPosition: () -> Float
) {
    Canvas(
        modifier.pointerInput(Unit) {
            this.detectTapGestures(
                onPress = { position ->
                    Log.d("UnlockCirecle", "Press at: $position")
                }
            )
        }
    ) {
        val strokeWidth = size.minDimension / 50
        val smallCircleRadius = size.minDimension / 15
        val keyRadius = size.minDimension / 25
        val margin = size.minDimension / 20 + smallCircleRadius
        val bigCircleRadius = (size.minDimension / 2f) - margin

        drawCircle(
            Color.Red,
            radius = bigCircleRadius,
            style = Stroke(width = strokeWidth)
        )
        degres.forEach { position ->
            drawLockPosition(position, smallCircleRadius, bigCircleRadius, strokeWidth)
        }
        drawKey(keyPosition, keyRadius, bigCircleRadius)
    }
}

private fun DrawScope.drawKey(
    keyPosition: () -> Float,
    keyRadius: Float,
    bigCircleRadius: Float
) {
    rotate(keyPosition()) {
        drawCircle(
            Color(0xFFFFC107),
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
    strokeWidth: Float
) {
    rotate(position) {
        drawCircle(
            Color(0x22FF0000),
            radius = smallCircleRadius,
            style = Fill,
            center = center.minus(Offset(0f, bigCircleRadius)),
            blendMode = BlendMode.Src
        )
        drawCircle(
            Color(0xFFFF0000),
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
            degres = listOf(0f, 90f, 135f),
            keyPosition = { 0f }
        )
    }
}

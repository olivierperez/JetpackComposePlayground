package fr.o80.testcompose.screen.pacman

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview

const val ANIMATION_DURATION = 150
const val BLOCS = 5
const val PERSO_SCALE = .7f
const val NUMBER_OF_GHOST_FEET = 3

@Composable
fun PacmanCanvas(
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition("pacman-animation")
    val mouthAngle by infinite.animateFloat(
        initialValue = 10f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ANIMATION_DURATION,
                easing = CubicBezierEasing(a = .7f, b = 0f, c = .71f, d = .86f)
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pacman-mouth"
    )
    val translationProgress by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = BLOCS * (2 * ANIMATION_DURATION),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "pacman-translation"
    )

    Canvas(modifier = modifier) {
        val blocSize = size.width / BLOCS
        val translationSize = size.width + (2 * blocSize)
        translate(left = translationProgress * translationSize - (2 * blocSize)) {
            drawGhost(blocSize)
            translate(left = blocSize) {
                drawPacman(mouthAngle, blocSize)
            }
        }
    }
}

private fun DrawScope.drawGhost(
    size: Float,
    ghostColor: Color = Color.Red
) {
    val margin = (size - size * PERSO_SCALE) / 2
    val feetSize = (size * PERSO_SCALE) / NUMBER_OF_GHOST_FEET / 2

    val path = Path().apply {
        moveTo(margin, size - margin - feetSize)
        lineTo(margin, size / 2)
        arcTo(
            rect = Rect(left = margin, top = margin, right = size - margin, bottom = size),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        lineTo(size - margin, size / 2)
        lineTo(size - margin, size - margin - feetSize)
        arcTo(
            rect = Rect(
                left = size - margin - feetSize * 2,
                top = size - margin - feetSize * 2,
                right = size - margin,
                bottom = size - margin
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        arcTo(
            rect = Rect(
                left = size - margin - feetSize * 4,
                top = size - margin - feetSize * 2,
                right = size - margin - feetSize * 2,
                bottom = size - margin
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        arcTo(
            rect = Rect(
                left = size - margin - feetSize * 6,
                top = size - margin - feetSize * 2,
                right = size - margin - feetSize * 4,
                bottom = size - margin
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        close()
    }
    drawPath(color = ghostColor, path = path)
}

private fun DrawScope.drawPacman(mouthAngle: Float, size: Float) {
    val margin = (size - size * PERSO_SCALE) / 2
    translate(left = margin, top = margin) {
        drawArc(
            color = Color.Yellow,
            size = Size(size * PERSO_SCALE, size * PERSO_SCALE),
            startAngle = mouthAngle,
            sweepAngle = 360f - 2 * mouthAngle,
            useCenter = true,
            style = Fill
        )
    }
}

@Preview
@Composable
private fun PacmanPreview() {
    PacmanCanvas(
        Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    )
}

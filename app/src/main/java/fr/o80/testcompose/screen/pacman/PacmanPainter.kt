package fr.o80.testcompose.screen.pacman

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview

class PacmanPainter : Painter() {

    override val intrinsicSize: Size = Size.Unspecified

    private val path = Path().apply {
//        moveTo(0f, 0f)
//        lineTo(100f, 100f)
        arcTo(
            rect = Rect(.2f, .2f, .8f, .8f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 360f,
            forceMoveTo = true
        )
        close()
    }

    override fun DrawScope.onDraw() {
        scale(
            scale = size.width,
            pivot = Offset(.5f, .5f)
        ) {
            drawPath(
                color = Color.Red,
                path = path,
                style = Stroke(width = .01f)
            )
        }
    }
}

@Preview
@Composable
private fun PacmanPainterPreview() {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        painter = PacmanPainter(),
        contentDescription = "Pacman"
    )
}

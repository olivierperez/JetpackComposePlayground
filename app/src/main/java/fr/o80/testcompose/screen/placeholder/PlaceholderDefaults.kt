package fr.o80.testcompose.screen.placeholder

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

object PlaceholderDefaults {
    val background: Brush = SolidColor(Color.LightGray)
    val highlightColor: Color = Color.LightGray.ligthen(.4f)
    val highlightSize: Float = 150f

    val animation: DurationBasedAnimationSpec<Float> = tween(
        durationMillis = 1500,
        easing = LinearEasing,
    )
    val repeatMode: RepeatMode = RepeatMode.Restart

    val margin: Float = 1.5f
}

private fun Color.ligthen(f: Float): Color {
    return Color(
        red = red + (1f - red) * f,
        green = green + (1f - green) * f,
        blue = blue + (1f - blue) * f,
        alpha = alpha
    )
}
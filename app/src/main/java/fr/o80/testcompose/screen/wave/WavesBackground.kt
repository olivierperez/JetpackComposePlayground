package fr.o80.testcompose.screen.wave

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KFunction4

fun Modifier.wavesBackground(
    colors: List<Color> = listOf(
        Color(0xff4A148C),
        Color(0xff7B1FA2)
    ),
    waveColors: Color = Color.White.copy(alpha = .1f),
    intensity1: Float = .05f,
    intensity2: Float = .1f,
    iterations: Float = 2f,
): Modifier {
    return this.drawWithCache {
        val gradientBrush = Brush.linearGradient(
            colors = colors,
            start = Offset(0f, size.height),
            end = Offset(size.width, 0f),
        )
        val wave1Path = generateWave(::f1, iterations, intensity1, top = -25f)
        val wave2Path = generateWave(::f2, iterations, intensity2, top = 75f)

        onDrawBehind {
            drawRect(gradientBrush)
            drawPath(wave1Path, waveColors)
            drawPath(wave2Path, waveColors)
        }
    }
}

private fun CacheDrawScope.generateWave(
    f: KFunction4<Int, Float, Float, Float, Float>,
    iterations: Float,
    intensity: Float,
    top: Float = 0f
): Path {
    return Path().apply {
        moveTo(0f, size.height / 2)
        repeat(101) { i ->
            val f1 = f(i, iterations, size.height, intensity)
            lineTo(size.width * i / 100, top + (size.height / 2) - f1)
        }
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        close()
    }
}

fun f1(
    i: Int,
    iterations: Float,
    height: Float,
    intensity: Float
): Float {
    val x = i * iterations * (3.14f * 2 / 100)
    val sins = sin(x / 1f) + cos(x / 2f) + sin(x / 4f)
    return sins * height * intensity
}

fun f2(
    i: Int,
    iterations: Float,
    height: Float,
    intensity: Float
): Float {
    val x = i * iterations * (3.14f * 2 / 100)
    val sins = sin(x / 1.5f) + sin(x / 3f) - sin(x / 4f)
    return sins * height * intensity
}

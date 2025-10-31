package fr.o80.testcompose.screen.brush

import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap

/**
 * A Brush that paints the left half black and the right half white.
 */
class HalfColorBrush(
    private val startColor: Color,
    private val endColor: Color
) : ShaderBrush() {
    val paint = Paint()
    override fun createShader(size: Size): Shader {
        val width = size.width.toInt().coerceAtLeast(2)
        val height = size.height.toInt().coerceAtLeast(1)
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        // Left half black
        paint.color = startColor.toArgb()
        canvas.drawRect(0f, 0f, width / 2f, height.toFloat(), paint)
        // Right half white
        paint.color = endColor.toArgb()
        canvas.drawRect(width / 2f, 0f, width.toFloat(), height.toFloat(), paint)
        return BitmapShader(
            bitmap,
            android.graphics.Shader.TileMode.CLAMP,
            android.graphics.Shader.TileMode.CLAMP
        )
    }
}
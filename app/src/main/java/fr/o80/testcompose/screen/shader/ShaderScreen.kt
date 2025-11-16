package fr.o80.testcompose.screen.shader

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.RuntimeShader
import android.os.Build
import android.view.animation.LinearInterpolator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShaderScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.Center)
                .glitch()
        ) {
            Text(
                text = "Shader Screen",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

private const val COLOR_SHADER_SRC = """
    layout(color) uniform half4 iColor;
    uniform half2 iResolution;
    uniform float iTime;
    
    uniform shader inputShader;
    
    half4 main(float2 fragCoord) {
        float4 pixel = inputShader.eval(fragCoord);
        return mix(pixel, iColor, abs(sin(iTime)));
    }
"""

@Composable
private fun Modifier.glitch(): Modifier {
    return if (Build.VERSION.SDK_INT >= 33) {
        var time by remember { mutableFloatStateOf(0f) }
        val shader = RuntimeShader(COLOR_SHADER_SRC)

        // Animator
        LaunchedEffect(Unit) {
            val duration = 5000f
            val animator = ValueAnimator.ofFloat(0f, 2f * Math.PI.toFloat())
            animator.duration = duration.toLong()
            animator.repeatMode = ValueAnimator.REVERSE
            animator.repeatCount = ValueAnimator.INFINITE
            animator.interpolator = LinearInterpolator()
            animator.addUpdateListener { animation ->
                time = animation.animatedValue as Float
            }
            animator.start()
        }

        this.drawWithCache {
            shader.setFloatUniform("iTime", time)
            shader.setColorUniform("iColor", Color.GREEN)
            shader.setFloatUniform("iResolution", size.width, size.height)

            onDrawWithContent {
                drawContent()
                drawRect(
                    brush = ShaderBrush(shader),
                    size = size,
                    blendMode = BlendMode.Overlay
                )
            }
        }
    } else {
        this
    }
}

@Preview
@Composable
private fun ShaderPreview() {
    Surface {
        ShaderScreen()
    }
}
package fr.o80.testcompose.screen.shader

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import android.view.animation.AccelerateInterpolator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun ShaderScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        var glitchEnabled: Boolean by remember { mutableStateOf(false) }
        Button(
            onClick = { glitchEnabled = !glitchEnabled },
            modifier = Modifier
                .align(Alignment.Center)
                .glitch(enabled = glitchEnabled)
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
    uniform float strength;
    uniform float strengthMultiplier;
    uniform float lineHeight;
    uniform float aberrationMultiplier;
    
    uniform shader inputShader;
    
    float rand(float2 co) {
        return fract(sin(dot(co, float2(12.9898, 78.233))) * 43758.5453);
    }
    
    float2 displacement(float2 fragCoord) {
        float row = floor(fragCoord.y / lineHeight) * lineHeight;
        float offset = (rand(float2(0, row)) - 0.5) * strength * strengthMultiplier;
        return float2(offset, 0);
    }
    
    half4 main(float2 fragCoord) {
        // Compute displacement
        
        float2 offset = displacement(fragCoord);
        
        // Compute chromatic aberration

        float aberration = strength * aberrationMultiplier;
        float2 redOffset = float2(aberration, 0.0);
        float2 blueOffset = float2(-aberration, 0.0);
        
        float red = inputShader.eval(fragCoord + offset + redOffset).r;
        float green = inputShader.eval(fragCoord + offset).g;
        float blue = inputShader.eval(fragCoord + offset + blueOffset).b;
        
        return half4(red, green, blue, inputShader.eval(fragCoord + offset).a);
    }
"""

@Composable
private fun Modifier.glitch(
    enabled: Boolean = true,
    duration: Duration = 1.seconds,
    repeatMode: Int = ValueAnimator.REVERSE,
    repeatCount: Int = ValueAnimator.INFINITE,
    interpolator: TimeInterpolator = AccelerateInterpolator(4f),
    lineHeight: Float = 5f,
    strengthMultiplier: Float = 50f,
    aberrationMultiplier: Float = 10f,
): Modifier {
    return if (Build.VERSION.SDK_INT >= 33) {
        var strength by remember { mutableFloatStateOf(0f) }
        val shader = RuntimeShader(COLOR_SHADER_SRC)
        val animator = remember { ValueAnimator.ofFloat(0f, 1f) }

        // Animator
        LaunchedEffect(enabled) {
            if (enabled) {
                animator.duration = duration.inWholeMilliseconds
                animator.repeatMode = repeatMode
                animator.repeatCount = repeatCount
                animator.interpolator = interpolator
                animator.addUpdateListener { animation ->
                    strength = animation.animatedValue as Float
                }
                animator.start()
            } else {
                animator.cancel()
                strength = 0f
                shader.setFloatUniform("strength", strength)
            }
        }

        this.graphicsLayer {
            renderEffect = RenderEffect
                .createRuntimeShaderEffect(shader, "inputShader")
                .also {
                    shader.setFloatUniform("lineHeight", lineHeight)
                    shader.setFloatUniform("strength", strength)
                    shader.setFloatUniform("strengthMultiplier", strengthMultiplier)
                    shader.setFloatUniform("aberrationMultiplier", aberrationMultiplier)
                }
                .asComposeRenderEffect()
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
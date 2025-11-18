package fr.o80.testcompose.screen.shader

import android.animation.ValueAnimator
import android.graphics.Color
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShaderScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
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
    uniform float strength;
    
    uniform shader inputShader;
    
    float strengthMultiplier = 50.0;
    float aberrationMultiplier = 10.0;
    float lineHeight = 5.0;
    
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
private fun Modifier.glitch(): Modifier {
    return if (Build.VERSION.SDK_INT >= 33) {
        var strength by remember { mutableFloatStateOf(0f) }
        val shader = RuntimeShader(COLOR_SHADER_SRC)

        // Animator
        LaunchedEffect(Unit) {
            val duration = 1000f
            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.duration = duration.toLong()
            animator.repeatMode = ValueAnimator.REVERSE
            animator.repeatCount = ValueAnimator.INFINITE
            animator.interpolator = AccelerateInterpolator(4f)
            animator.addUpdateListener { animation ->
                strength = animation.animatedValue as Float
            }
            animator.start()
        }

        this.graphicsLayer {
            renderEffect = RenderEffect
                .createRuntimeShaderEffect(shader, "inputShader")
                .also {
                    shader.setFloatUniform("strength", strength)
                    shader.setColorUniform("iColor", Color.RED)
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
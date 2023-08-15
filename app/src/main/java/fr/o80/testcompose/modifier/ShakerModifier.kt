package fr.o80.testcompose.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.shaker(shakerController: ShakerController) = composed {
    shakerController.config?.let { config ->
        val shake = remember { Animatable(0f) }
        
        LaunchedEffect(shakerController.config) {
            for(i in 0 until config.iterations) {
                shake.animateTo(1f, spring(stiffness = config.intensity))
                shake.animateTo(-1f, spring(stiffness = config.intensity))
            }
            shake.animateTo(0f)
        }
        
        this.graphicsLayer {
            rotationX = shake.value * config.rotateX
            rotationY = shake.value * config.rotateY
            rotationZ = shake.value * config.rotateZ

            scaleX = 1f + (shake.value * config.scaleX)
            scaleY = 1f + (shake.value * config.scaleY)

            translationX = shake.value * config.translateX
            translationY = shake.value * config.translateY
        }
    } ?: this
}

@Composable
fun rememberShakerState(): ShakerController {
    return remember { ShakerController() }
}

class ShakerController {
    var config: ShakeConfig? by mutableStateOf(null)
        private set

    fun shake(config: ShakeConfig) {
        this.config = config
    }
}

data class ShakeConfig(
    val iterations: Int,
    val intensity: Float = 1_000f,
    val rotateX: Float = 0f,
    val rotateY: Float = 0f,
    val rotateZ: Float = 0f,
    val scaleX: Float = 0f,
    val scaleY: Float = 0f,
    val translateX: Float = 0f,
    val translateY: Float = 0f,
    val trigger: Long = System.currentTimeMillis(),
)

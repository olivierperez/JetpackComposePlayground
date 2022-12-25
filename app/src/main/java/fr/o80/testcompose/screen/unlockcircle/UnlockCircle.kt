package fr.o80.testcompose.screen.unlockcircle

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme
import kotlin.random.Random

@Composable
fun UnlockCircle(
    modifier: Modifier,
    onClose: () -> Unit
) {
    Box(modifier) {
        val infiniteTransition = rememberInfiniteTransition()
        val keyPosition by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )

        )
        val degres = remember { listOf(1, 2, 3).map { Random.nextFloat() * 360 } }
        Lock(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f),
            degres = degres,
            keyPosition = { keyPosition }
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
    }
}

@Preview
@Composable
fun UnlockCirclePreview() {
    TestComposeCanvasTheme {
        UnlockCircle(
            Modifier.fillMaxSize(),
            onClose = {}
        )
    }
}
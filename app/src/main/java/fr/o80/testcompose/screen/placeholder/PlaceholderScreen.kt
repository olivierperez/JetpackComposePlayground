package fr.o80.testcompose.screen.placeholder

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlaceholderScreen(
    modifier: Modifier = Modifier
) {
    val infiniteAnimation = remember {
        infiniteRepeatable(
            animation = PlaceholderDefaults.animation,
            repeatMode = PlaceholderDefaults.repeatMode
        )
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Classic")
        Box(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .placeholder(
                    clip = RoundedCornerShape(8.dp),
                    animationSpec = infiniteAnimation
                )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                Modifier
                    .size(200.dp, 24.dp)
                    .placeholder(
                        clip = RoundedCornerShape(8.dp),
                        animationSpec = infiniteAnimation
                    )
            )
            Box(
                Modifier
                    .size(50.dp, 24.dp)
                    .placeholder(
                        clip = RoundedCornerShape(8.dp),
                        animationSpec = infiniteAnimation
                    )
            )
        }
        Text("Customized")
        Box(
            Modifier
                .size(128.dp, 48.dp)
                .placeholder(
                    background = SolidColor(Color.Red),
                    highlightColor = Color.Cyan,
                    clip = RoundedCornerShape(8.dp),
                    margin = .2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1500,
                            easing = EaseInOutCubic,
                        ),
                        repeatMode = RepeatMode.Reverse
                    )
                )
        )
    }
}

@Preview
@Composable
private fun PlaceholderScreenPreview() {
    PlaceholderScreen()
}
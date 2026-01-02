package fr.o80.testcompose.screen.recomposition

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun RecompositionCount(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = spacedBy(16.dp)
    ) {
        var text by remember { mutableStateOf("Example") }
        val infiniteTransition = rememberInfiniteTransition()
        val animatedAlpha by infiniteTransition.animateFloat(
            initialValue = .3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse,
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                // Trigger a lot of recompositions
                //.alpha(animatedAlpha)

                // Do not trigger recompositions
                .graphicsLayer {
                    this.alpha = animatedAlpha
                }
                .trackRecompositions()
        )
        Text(
            text = "Go to Layout Inspector and check the \"RecompositionCount\" semantic property of the text above.",
            style = MaterialTheme.typography.labelMedium,
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                text = if (text == "Example") "Changed" else "Example"
            }) {
                Text("Change Text")
            }
        }
    }
}

val RecompositionCountKey = SemanticsPropertyKey<Int>("RecompositionCount")
var SemanticsPropertyReceiver.recompositionCount by RecompositionCountKey

/**
 * Modifier from https://medium.com/catching-excessive-recompositions-in-jetpack-compose-with-tests-8d0b952e2853
 */
fun Modifier.trackRecompositions(): Modifier = composed {
    var recompositions by remember { mutableIntStateOf(0) }

    SideEffect {
        recompositions++
    }

    Modifier
        .semantics {
            recompositionCount = recompositions
        }
}
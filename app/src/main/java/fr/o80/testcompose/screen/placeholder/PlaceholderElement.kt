package fr.o80.testcompose.screen.placeholder

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Modifier.placeholder(
    background: Brush = PlaceholderDefaults.background,
    highlightColor: Color = PlaceholderDefaults.highlightColor,
    highlightSize: Float = PlaceholderDefaults.highlightSize,
    margin: Float = PlaceholderDefaults.margin,
    clip: Shape = RectangleShape,
    animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = PlaceholderDefaults.animation,
        repeatMode = PlaceholderDefaults.repeatMode
    )
): Modifier {
    val state = remember {
        PlaceholderState(
            background = background,
            highlightColor = highlightColor,
            highlightSize = highlightSize,
            margin = margin,
            animationSpec = animationSpec
        )
    }
    val scope = rememberCoroutineScope()

    DisposableEffect(state) {
        scope.launch { state.startAnimation() }
        onDispose {
            scope.launch { state.stopAnimation() }
        }
    }

    return this
        .clip(clip)
        .then(PlaceholderElement(state))
}

private class PlaceholderState(
    val background: Brush,
    val highlightColor: Color,
    val highlightSize: Float,
    val margin: Float,
    private val animationSpec: InfiniteRepeatableSpec<Float>
) {
    val progress = Animatable(0f)

    val alpha = Animatable(1f)

    suspend fun startAnimation() {
        alpha.animateTo(1f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = animationSpec
        )
    }

    suspend fun stopAnimation() {
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = spring(Spring.DampingRatioNoBouncy, Spring.StiffnessLow),
        )
        progress.stop()
    }
}

private data class PlaceholderElement(
    private val state: PlaceholderState
) : ModifierNodeElement<PlaceholderModifier>() {
    override fun create(): PlaceholderModifier {
        return PlaceholderModifier(state)
    }

    override fun update(node: PlaceholderModifier) {
        node.state = state
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "placeholder"
        properties["state"] = state
    }
}

private class PlaceholderModifier(
    var state: PlaceholderState,
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        val progressOffset =
            (size.width * (1 + state.margin) * state.progress.value) -
                    (size.width * (state.margin) / 2f) -
                    state.highlightSize / 2f
        drawContent()
        drawRect(
            brush = state.background,
            alpha = state.alpha.value
        )
        translate(progressOffset) {
            drawRect(
                brush = Brush.linearGradient(
                    0f to Color.Transparent,
                    .5f to state.highlightColor,
                    1f to Color.Transparent,
                    start = Offset(0f, 0f),
                    end = Offset(state.highlightSize, 0f)
                ),
                alpha = state.alpha.value,
            )
        }
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    Box(
        Modifier
            .size(300.dp, 150.dp)
            .placeholder(
                background = SolidColor(Color.Red)
            )
    ) {
        Text("Loading...")
    }
}
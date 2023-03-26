package fr.o80.testcompose.screen.twoTopAppBar

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.modifier.maxHeight

@ExperimentalMaterial3Api
@Composable
fun TwoTopAppBar(
    modifier: Modifier,
    maxHeight: Dp = 250.dp,
    collapsedContent: @Composable (modifier: Modifier) -> Unit,
    expandedContent: @Composable (modifier: Modifier) -> Unit,
    alphaEasing: Easing = CubicBezierEasing(.8f, 0f, .8f, .15f),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val localDensity = LocalDensity.current
    var smallHeight by remember { mutableStateOf(0f) }
    val maxHeightPx = with(localDensity) { maxHeight.toPx() }

    LaunchedEffect(smallHeight) {
        if (scrollBehavior?.state?.heightOffsetLimit != smallHeight - maxHeightPx) {
            scrollBehavior?.state?.heightOffsetLimit = smallHeight - maxHeightPx
        }
    }

    val colorTransitionFraction by remember {
        derivedStateOf { scrollBehavior?.state?.collapsedFraction ?: 0f }
    }
    val smallBarAlpha by remember { derivedStateOf { alphaEasing.transform(colorTransitionFraction) } }
    val largeBarAlpha by remember { derivedStateOf { 1f - smallBarAlpha } }

    Box(modifier) {
        expandedContent(
            Modifier
                .graphicsLayer { this.alpha = largeBarAlpha }
                .maxHeight { maxHeightPx + (scrollBehavior?.state?.heightOffset ?: 0f) }
        )
        collapsedContent(
            Modifier
                .graphicsLayer { this.alpha = smallBarAlpha }
                .onGloballyPositioned {
                    smallHeight = with(localDensity) {
                        it.size.height
                            .toDp()
                            .toPx()
                    }
                }
        )
    }
}

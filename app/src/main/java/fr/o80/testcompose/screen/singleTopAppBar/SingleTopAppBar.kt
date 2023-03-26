package fr.o80.testcompose.screen.singleTopAppBar

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.modifier.height

@ExperimentalMaterial3Api
@Composable
fun SingleTopAppBar(
    modifier: Modifier,
    title: String,
    titleColor: Color,
    maxHeight: Dp,
    navigationIcon: @Composable (Modifier) -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
    var navigationIconSize by remember { mutableStateOf(IntSize(0, 0)) }
    var titleHeightPx by remember { mutableStateOf(0f) }
    val x1 by remember(navigationIconSize.width) { mutableStateOf(navigationIconSize.width * 4f / 4) }

    LaunchedEffect(navigationIconSize) {
        if (scrollBehavior.state.heightOffsetLimit != navigationIconSize.height - maxHeightPx) {
            scrollBehavior.state.heightOffsetLimit = navigationIconSize.height - maxHeightPx
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets)
            .height { maxHeightPx + scrollBehavior.state.heightOffset }
    ) {
        navigationIcon(
            Modifier.onSizeChanged { navigationIconSize = it }
        )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = titleColor,
            modifier = Modifier
                .graphicsLayer {
                    val collapseRange: Float = (maxHeightPx - navigationIconSize.height)
                    val collapseFraction: Float =
                        (-scrollBehavior.state.heightOffset / collapseRange).coerceIn(0f, 1f)

                    val titleDestinationX = navigationIconSize.width.toFloat()
                    val titleDestinationY =
                        (navigationIconSize.height.toFloat() - titleHeightPx) / 2

                    val titleOriginX = 0f
                    val titleOriginY = maxHeightPx - titleHeightPx

                    val xLerp1 = lerp(
                        titleOriginX,
                        x1,
                        collapseFraction
                    )
                    val xLerp2 = lerp(
                        x1,
                        titleDestinationX,
                        collapseFraction
                    )
                    val titleX = lerp(xLerp1, xLerp2, collapseFraction)

                    Log.d("OPZ", "-------------")
                    Log.d("OPZ", "x1: $x1")
                    Log.d("OPZ", "xLerp1: $xLerp1")
                    Log.d("OPZ", "xLerp2: $xLerp2")
                    Log.d("OPZ", "titleX: $titleX")

                    val titleY = lerp(
                        titleOriginY,
                        titleDestinationY,
                        collapseFraction
                    )

                    translationX = titleX
                    translationY = titleY
                }
                .onGloballyPositioned {
                    titleHeightPx = it.size.height.toFloat()
                }
                .padding(8.dp)
        )
    }
}

private fun lerp(originX: Float, destinationX: Float, fraction: Float): Float {
    return originX + (destinationX - originX) * fraction
}

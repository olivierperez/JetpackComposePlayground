package fr.o80.testcompose.screen.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme
import kotlin.math.ceil

class BottomBarScope

@Composable
fun BottomBar(
    fab: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = BottomBarDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: Dp = BottomBarDefaults.elevation,
    fabPadding: Dp = BottomBarDefaults.fabPadding,
    contentPadding: PaddingValues = BottomBarDefaults.contentPadding,
    windowInsets: WindowInsets = BottomBarDefaults.windowInsets,
    content: @Composable BottomBarScope.() -> Unit
) {
    val direction = LocalLayoutDirection.current
    val density = LocalDensity.current
    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    val totalPadding = contentPadding + windowInsets.asPaddingValues(density)

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Layout(
            modifier = modifier
                .shadow(elevation, clip = false)
                .background(containerColor, shape),
            measurePolicy = { measurables, constraints ->
                val fabPlaceable = measurables[0].measure(Constraints())
                val contentPlaceables = measurables.drop(1).map {
                    it.measure(constraints.copy(minWidth = 0))
                }
                val startCount = ceil(contentPlaceables.size / 2f).toInt()
                val endCount = contentPlaceables.size - startCount
                val startPlaceables = contentPlaceables.take(startCount)
                val endPlaceables = contentPlaceables.drop(startCount)

                val topPadding = totalPadding.calculateTopPadding().toIntPx(density)
                val bottomPadding = totalPadding.calculateBottomPadding().toIntPx(density)

                val width = constraints.maxWidth
                val height =
                    (contentPlaceables.maxOfOrNull { it.height } ?: 16) + topPadding + bottomPadding

                val fabPaddingPx = fabPadding.toIntPx(density)
                val startPaddingPx = totalPadding.calculateStartPadding(direction).toIntPx(density)
                val endPaddingPx = totalPadding.calculateEndPadding(direction).toIntPx(density)

                val startWidth =
                    (width / 2) - (fabPlaceable.width / 2) - startPaddingPx - fabPaddingPx
                val startWidths = startPlaceables.sumOf { it.width }
                val startSpace = ((startWidth - startWidths) / (startCount + 1)).coerceAtLeast(0)

                val endStart = (width / 2) + (fabPlaceable.width / 2) + fabPaddingPx
                val endWidth = (width / 2) - (fabPlaceable.width / 2) - endPaddingPx - fabPaddingPx
                val endWidths = endPlaceables.sumOf { it.width }
                val endSpace = ((endWidth - endWidths) / (endCount + 1)).coerceAtLeast(0)

                val heightCenter = topPadding + (height - bottomPadding - topPadding) / 2

                layout(width, height) {
                    fabPlaceable.place(
                        (width / 2) - (fabPlaceable.width / 2),
                        fabPlaceable.height / -2
                    )

                    placeItems(
                        placeables = startPlaceables,
                        start = startPaddingPx + startSpace,
                        space = startSpace,
                        heightCenter = heightCenter
                    )

                    placeItems(
                        placeables = endPlaceables,
                        start = endStart + endSpace,
                        space = endSpace,
                        heightCenter = heightCenter
                    )
                }
            },
            content = {
                fab()
                with(BottomBarScope()) {
                    content()
                }
            }
        )
    }
}

private fun Placeable.PlacementScope.placeItems(
    placeables: List<Placeable>,
    start: Int,
    space: Int,
    heightCenter: Int
) {
    var currentPosition = start
    placeables.forEach { content ->
        content.place(currentPosition, heightCenter - (content.height / 2))
        currentPosition += content.width + space
    }
}

private fun Dp.toIntPx(density: Density) = with(density) { toPx().toInt() }

@Preview
@Composable
private fun BottomBarPreview() {
    TestComposeCanvasTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            BottomBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                fab = {
                    FloatingActionButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Build, contentDescription = null)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current
    return PaddingValues(
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(direction) + other.calculateEndPadding(direction),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
        start = calculateStartPadding(direction) + other.calculateStartPadding(direction)
    )
}

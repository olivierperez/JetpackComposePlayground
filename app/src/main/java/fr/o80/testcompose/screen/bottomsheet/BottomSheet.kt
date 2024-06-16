@file:OptIn(ExperimentalFoundationApi::class)

package fr.o80.testcompose.screen.bottomsheet

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomSheet(
    behavior: BottomSheetBehavior<out Any>,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .wrapContentSize()
            .anchoredDraggable(
                behavior.anchoredDraggableState,
                Orientation.Vertical,
            )
    ) {
        BottomSheetLayout(
            behavior = behavior,
            modifier = Modifier
        ) {
            header()
            content()
            footer()
        }
    }
}

@Composable
fun BottomSheetLayout(
    behavior: BottomSheetBehavior<out Any>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .nestedScroll(rememberBottomSheetNestedScrollConnection(behavior)),
        content = content,
        measurePolicy = { measurables, constraints ->
            assert(measurables.size == 3) {
                "A BottomSheet must have 3 composables but have ${measurables.size}"
            }

            val headerPlaceable = measurables[0].measure(constraints)
            val constraintsBelowHeader =
                constraints.copy(maxHeight = constraints.maxHeight - headerPlaceable.height)

            val footerPlaceable = measurables[2].measure(constraintsBelowHeader)
            val constrainsBetweenHeaderAndFooter =
                constraintsBelowHeader.copy(maxHeight = constraintsBelowHeader.maxHeight - footerPlaceable.height)

            val contentPlaceable = measurables[1].measure(constrainsBetweenHeaderAndFooter)

            behavior.updateHeights(
                headerPlaceable.height,
                contentPlaceable.height,
                footerPlaceable.height
            )

            val fullHeight =
                headerPlaceable.height + contentPlaceable.height + footerPlaceable.height
            val anchorOffset =
                if (behavior.anchoredDraggableState.offset.isNaN()) 0f
                else behavior.anchoredDraggableState.offset
            val height = (fullHeight - anchorOffset.roundToInt()).coerceAtLeast(0)

            println("fullHeight: $fullHeight")
            println("anchorOffset: $anchorOffset")
            println("height=(full-anchorOffset): $height")

            layout(constraints.maxWidth, height) {
                println("Offset: ${behavior.anchoredDraggableState.offset}")
                behavior.place(height, headerPlaceable, contentPlaceable, footerPlaceable)
            }
        }
    )
}

interface BottomSheetBehavior<State> {
    val anchoredDraggableState: AnchoredDraggableState<State>
    fun updateHeights(headerHeight: Int, contentHeight: Int, footerHeight: Int)

    context(Placeable.PlacementScope)
    fun place(height: Int, header: Placeable, content: Placeable, footer: Placeable)
}

@Composable
fun rememberHeaderFooterFirstBehavior(
    initialState: HeaderFooterFirsState
): HeaderFooterFirstBehavior {
    val density = LocalDensity.current
    val positionalThreshold = remember(density) { with(density) { 40.dp.toPx() } }
    val velocityThreshold = remember(density) { with(density) { 125.dp.toPx() } }

    val draggableState = rememberSaveable(
        initialState,
        saver = AnchoredDraggableState.Saver(
            animationSpec = SpringSpec(),
            positionalThreshold = { positionalThreshold },
            velocityThreshold = { velocityThreshold }
        )
    ) {
        AnchoredDraggableState(
            initialValue = initialState,
            anchors = DraggableAnchors { /* No anchors at init */ },
            positionalThreshold = { positionalThreshold },
            velocityThreshold = { velocityThreshold },
            animationSpec = SpringSpec()
        )
    }

    return remember {
        HeaderFooterFirstBehavior(
            draggableState
        )
    }
}

class HeaderFooterFirstBehavior(
    override val anchoredDraggableState: AnchoredDraggableState<HeaderFooterFirsState>
) : BottomSheetBehavior<HeaderFooterFirsState> {
    override fun updateHeights(headerHeight: Int, contentHeight: Int, footerHeight: Int) {
        val fullHeight = headerHeight + contentHeight + footerHeight
        anchoredDraggableState.updateAnchors(
            DraggableAnchors {
                HeaderFooterFirsState.Full at 0f
                HeaderFooterFirsState.HeaderFooter at contentHeight.toFloat()
                HeaderFooterFirsState.Header at (fullHeight - headerHeight).toFloat()
            }
        )
        println("headerHeight: $headerHeight")
        println("contentHeight: $contentHeight")
        println("footerHeight: $footerHeight")
        println("anchors: ${anchoredDraggableState.anchors}")
    }

    context(Placeable.PlacementScope)
    override fun place(height: Int, header: Placeable, content: Placeable, footer: Placeable) {
        header.place(x = 0, y = 0)
        content.place(x = 0, y = header.height)
        footer.place(
            x = 0,
            y = if (height > header.height + footer.height) {
                height - footer.height
            } else header.height,
            zIndex = 1f
        )
    }
}

enum class HeaderFooterFirsState {
    Header,
    HeaderFooter,
    Full
}

@file:OptIn(ExperimentalFoundationApi::class)

package fr.o80.testcompose.screen.bottomsheet.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomSheet(
    state: BottomSheetState<out Any>,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .wrapContentSize()
            .anchoredDraggable(
                state.behavior.anchoredDraggableState,
                Orientation.Vertical,
            )
    ) {
        BottomSheetLayout(
            behavior = state.behavior,
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

            layout(constraints.maxWidth, height) {
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



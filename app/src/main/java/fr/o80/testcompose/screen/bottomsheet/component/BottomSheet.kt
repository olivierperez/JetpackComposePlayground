@file:OptIn(ExperimentalFoundationApi::class)

package fr.o80.testcompose.screen.bottomsheet.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrain
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomSheet(
    state: BottomSheetState<out Any>,
    paneTitle: String,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val stateDescription by state.behavior.stateDescription
    Surface(
        modifier = modifier
            .wrapContentSize()
            .anchoredDraggable(
                state.behavior.anchoredDraggableState,
                Orientation.Vertical,
            )
            .semantics {
                println("OPZ semantics stateDescription: $stateDescription")
                this.paneTitle = paneTitle
                this.stateDescription = stateDescription
            }
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
                constraints.constrain(
                    Constraints(maxHeight = constraints.maxHeight - headerPlaceable.height)
                )

            val footerPlaceable = measurables[2].measure(constraintsBelowHeader)
            val constrainsBetweenHeaderAndFooter =
                constraintsBelowHeader.constrain(
                    Constraints(maxHeight = constraintsBelowHeader.maxHeight - footerPlaceable.height)
                )

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

@Stable
interface BottomSheetBehavior<StateClass> {
    val anchoredDraggableState: AnchoredDraggableState<StateClass>
    val isExpanded: State<Boolean>
    val stateDescription: State<String>

    fun updateHeights(headerHeight: Int, contentHeight: Int, footerHeight: Int)

    context(Placeable.PlacementScope)
    fun place(height: Int, header: Placeable, content: Placeable, footer: Placeable)
}



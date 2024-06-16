package fr.o80.testcompose.screen.bottomsheet.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <State> rememberBottomSheetNestedScrollConnection(
    behavior: BottomSheetBehavior<State>
): NestedScrollConnection = remember(behavior) {
    ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
        behavior = behavior,
        orientation = Orientation.Vertical,
        onFling = {}
    )
}

/**
 * @see androidx.compose.material3.ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection
 */
@ExperimentalFoundationApi
class ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection<State>(
    private val behavior: BottomSheetBehavior<State>,
    private val orientation: Orientation,
    private val onFling: (velocity: Float) -> Unit
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.toFloat()
        return if (delta < 0 && source == NestedScrollSource.Drag) {
            behavior.anchoredDraggableState.dispatchRawDelta(delta).toOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return if (source == NestedScrollSource.Drag) {
            behavior.anchoredDraggableState.dispatchRawDelta(available.toFloat())
                .toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = available.toFloat()
        val currentOffset = behavior.anchoredDraggableState.requireOffset()
        val minAnchor = behavior.anchoredDraggableState.anchors.minAnchor()
        return if (toFling < 0 && currentOffset > minAnchor) {
            onFling(toFling)
            // since we go to the anchor with tween settling, consume all for the best UX
            available
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        onFling(available.toFloat())
        return available
    }

    private fun Float.toOffset(): Offset = Offset(
        x = if (orientation == Orientation.Horizontal) this else 0f,
        y = if (orientation == Orientation.Vertical) this else 0f
    )

    @JvmName("velocityToFloat")
    private fun Velocity.toFloat() = if (orientation == Orientation.Horizontal) x else y

    @JvmName("offsetToFloat")
    private fun Offset.toFloat(): Float = if (orientation == Orientation.Horizontal) x else y
}

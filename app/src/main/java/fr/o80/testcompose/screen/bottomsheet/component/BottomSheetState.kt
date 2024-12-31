@file:OptIn(ExperimentalFoundationApi::class)

package fr.o80.testcompose.screen.bottomsheet.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.abs

@Composable
fun <State> rememberBottomSheetState(
    behavior: BottomSheetBehavior<State>,
    onStateChanged: (State) -> Unit = {}
): BottomSheetState<State> {
    LaunchedEffect(behavior.anchoredDraggableState.currentValue) {
        onStateChanged(behavior.anchoredDraggableState.currentValue)
    }
    return remember { BottomSheetState(behavior) }
}

class BottomSheetState<State>(
    val behavior: BottomSheetBehavior<State>
) {
    suspend fun scrollTo(state: State) {
        behavior.anchoredDraggableState.animateTo(state)
    }

    fun progressBetween(start: State, end: State): Float {
        val positionOfStart = behavior.anchoredDraggableState.anchors.positionOf(start)
        val positionOfEnd = behavior.anchoredDraggableState.anchors.positionOf(end)
        val currentPosition = behavior.anchoredDraggableState.offset

        val progressionFromStart =
            (currentPosition - positionOfStart) / (positionOfEnd - positionOfStart)

        return abs(
            if (progressionFromStart.isNaN()) {
                0f
            } else {
                progressionFromStart.coerceIn(0f, 1f)
            }
        )
    }
}

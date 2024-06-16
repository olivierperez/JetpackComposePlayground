@file:OptIn(ExperimentalFoundationApi::class)

package fr.o80.testcompose.screen.bottomsheet.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

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
}

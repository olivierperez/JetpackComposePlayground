@file:OptIn(ExperimentalFoundationApi::class)

package fr.o80.testcompose.screen.bottomsheet.component.behavior

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.screen.bottomsheet.component.BottomSheetBehavior

@Composable
fun rememberHeaderFooterFirstBehavior(
    initialState: HeaderFooterFirstState,
    stateDescriptions: HeaderFooterFirstStateDescriptions
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
            draggableState,
            stateDescriptions
        )
    }
}

class HeaderFooterFirstBehavior(
    override val anchoredDraggableState: AnchoredDraggableState<HeaderFooterFirstState>,
    private val stateDescriptions: HeaderFooterFirstStateDescriptions
) : BottomSheetBehavior<HeaderFooterFirstState> {

    override val isExpanded: State<Boolean> by derivedStateOf {
        mutableStateOf(anchoredDraggableState.currentValue == HeaderFooterFirstState.Full)
    }

    override val stateDescription: State<String> by derivedStateOf {
        mutableStateOf(
            when (anchoredDraggableState.currentValue) {
                HeaderFooterFirstState.Header -> stateDescriptions.header
                HeaderFooterFirstState.HeaderFooter -> stateDescriptions.headerFooter
                HeaderFooterFirstState.Full -> stateDescriptions.full
            }
        )
    }

    override fun updateHeights(headerHeight: Int, contentHeight: Int, footerHeight: Int) {
        val fullHeight = headerHeight + contentHeight + footerHeight
        anchoredDraggableState.updateAnchors(
            DraggableAnchors {
                HeaderFooterFirstState.Full at 0f
                HeaderFooterFirstState.HeaderFooter at contentHeight.toFloat()
                HeaderFooterFirstState.Header at (fullHeight - headerHeight).toFloat()
            }
        )
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

enum class HeaderFooterFirstState {
    Header,
    HeaderFooter,
    Full
}

data class HeaderFooterFirstStateDescriptions(
    val full: String,
    val headerFooter: String,
    val header: String
)

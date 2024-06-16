@file:OptIn(ExperimentalFoundationApi::class)

package fr.o80.testcompose.screen.bottomsheet.component.behavior

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.screen.bottomsheet.component.BottomSheetBehavior

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

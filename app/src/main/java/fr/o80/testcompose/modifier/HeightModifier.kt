package fr.o80.testcompose.modifier

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints

class HeightModifier(
    private val heightPx: () -> Float
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val height = heightPx().toInt()
        val newConstraints = constraints.copy(minHeight = height, maxHeight = height)
        val placeable = measurable.measure(newConstraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

@Stable
fun Modifier.height(heightPx: () -> Float) = this.then(
    HeightModifier(heightPx)
)
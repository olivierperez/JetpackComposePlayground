package fr.o80.testcompose.screen.bottombar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object BottomBarDefaults {
    val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainer

    val elevation: Dp = 6.dp

    val fabPadding: Dp = 8.dp

    val contentPadding: PaddingValues = PaddingValues(8.dp)
}

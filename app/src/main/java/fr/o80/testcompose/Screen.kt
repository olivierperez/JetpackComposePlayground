package fr.o80.testcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.o80.testcompose.screen.unlockcircle.UnlockCircle

enum class Screen(
    val label: String,
    val render: @Composable (modifier: Modifier, onClose: () -> Unit) -> Unit
) {
    LOCK_CIRCLE(
        label = "Lock Circle",
        render = { modifier, onClose -> UnlockCircle(modifier, onClose) }
    )
}
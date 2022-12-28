package fr.o80.testcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.o80.testcompose.screen.appearingtopbar.AppearingTopBar
import fr.o80.testcompose.screen.customtabbar.CustomTopAppBarScreen
import fr.o80.testcompose.screen.pushedfooter.PushedFooter
import fr.o80.testcompose.screen.unlockcircle.UnlockCircle

enum class Screen(
    val label: String,
    val render: @Composable (modifier: Modifier, onClose: () -> Unit) -> Unit
) {
    APPEARING_TOP_BAR(
        label = "Appearing Top App Bar",
        render = { modifier, onClose -> AppearingTopBar(modifier, onClose) }
    ),
    CUSTOM_TAB_BAR(
        label = "Custom Top App Bar",
        render = { modifier, onClose -> CustomTopAppBarScreen(modifier, onClose) }
    ),
    LOCK_CIRCLE(
        label = "Lock Circle",
        render = { modifier, onClose -> UnlockCircle(modifier, onClose) }
    ),
    PUSHED_FOOTER(
        label = "Pushed Footer",
        render = { modifier, onClose -> PushedFooter(modifier, onClose) }
    )
}
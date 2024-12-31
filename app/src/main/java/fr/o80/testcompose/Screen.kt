package fr.o80.testcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.o80.testcompose.screen.ScreenWrapper
import fr.o80.testcompose.screen.appearingtopbar.AppearingAppBarScreen
import fr.o80.testcompose.screen.bottomsheet.BottomSheetScreen
import fr.o80.testcompose.screen.pacman.Pacmans
import fr.o80.testcompose.screen.pushedfooter.PushedFooterScreen
import fr.o80.testcompose.screen.shaker.ShakerScreen
import fr.o80.testcompose.screen.singleTopAppBar.SingleTopAppBarScreen
import fr.o80.testcompose.screen.twoTopAppBar.TwoTopAppBarScreen
import fr.o80.testcompose.screen.unlockcircle.UnlockCircleScreen
import fr.o80.testcompose.screen.wave.WavesBackground

fun interface Rendering {
    @Composable
    fun Render(modifier: Modifier, onClose: () -> Unit)
}

enum class Screen(
    val label: String,
    val rendering: Rendering
) {
    APPEARING_APP_BAR(
        label = "Appearing App Bar",
        rendering = { modifier, onClose -> AppearingAppBarScreen(modifier, onClose) }
    ),
    SINGLE_TOP_APP_BAR(
        label = "Single Top App Bar",
        rendering = { modifier, onClose -> SingleTopAppBarScreen(modifier, onClose) }
    ),
    TWO_TOP_APP_BAR(
        label = "Two Top App Bar",
        rendering = { modifier, onClose -> TwoTopAppBarScreen(modifier, onClose) }
    ),
    LOCK_CIRCLE(
        label = "Lock Circle",
        rendering = { modifier, onClose -> UnlockCircleScreen(modifier, onClose) }
    ),
    PUSHED_FOOTER(
        label = "Pushed Footer",
        rendering = { modifier, onClose -> PushedFooterScreen(modifier, onClose) }
    ),
    SHAKER(
        label = "Shaker",
        rendering = { modifier, onClose -> ShakerScreen(modifier, onClose) }
    ),
    WAVE_BACKGROUND(
        label = "Wave Background",
        rendering = { modifier, onClose ->
            ScreenWrapper(
                WAVE_BACKGROUND.label,
                modifier,
                onClose
            ) { WavesBackground() }
        }
    ),
    PACMAN(
        label = "Pacman",
        rendering = { modifier, onClose ->
            ScreenWrapper(
                PACMAN.label,
                modifier,
                onClose
            ) { Pacmans() }
        }
    ),
    BOTTOM_SHEET(
        label = "Bottom Sheet",
        rendering = { modifier, onClose ->
            ScreenWrapper(
                BOTTOM_SHEET.label,
                modifier,
                onClose
            ) { BottomSheetScreen() }
        }
    )
}

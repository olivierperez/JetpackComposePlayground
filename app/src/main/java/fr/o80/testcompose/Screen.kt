package fr.o80.testcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.o80.testcompose.screen.ScreenWrapper
import fr.o80.testcompose.screen.appearingtopbar.AppearingAppBarScreen
import fr.o80.testcompose.screen.async.AsyncContentLoaderScreen
import fr.o80.testcompose.screen.bottombar.BottomBarScreen
import fr.o80.testcompose.screen.bottomsheet.BottomSheetScreen
import fr.o80.testcompose.screen.brush.HalfColorScreen
import fr.o80.testcompose.screen.pacman.Pacmans
import fr.o80.testcompose.screen.placeholder.PlaceholderScreen
import fr.o80.testcompose.screen.pushedfooter.PushedFooterScreen
import fr.o80.testcompose.screen.shader.ShaderScreen
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
        rendering = Rendering { modifier, onClose -> AppearingAppBarScreen(modifier, onClose) }
    ),
    ASYNC_CONTENT_LOADER(
        label = "Async Content Loader",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                ASYNC_CONTENT_LOADER.label,
                modifier,
                onClose
            ) { AsyncContentLoaderScreen() }
        }
    ),
    HALF_COLOR_BRUSH(
        label = "Half Color Brush",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                HALF_COLOR_BRUSH.label,
                modifier,
                onClose
            ) { HalfColorScreen() }
        }
    ),
    PLACEHOLDER(
        label = "Placeholder",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                PLACEHOLDER.label,
                modifier,
                onClose
            ) { PlaceholderScreen() }
        }
    ),
    SINGLE_TOP_APP_BAR(
        label = "Single Top App Bar",
        rendering = Rendering { modifier, onClose -> SingleTopAppBarScreen(modifier, onClose) }
    ),
    TWO_TOP_APP_BAR(
        label = "Two Top App Bar",
        rendering = Rendering { modifier, onClose -> TwoTopAppBarScreen(modifier, onClose) }
    ),
    LOCK_CIRCLE(
        label = "Lock Circle",
        rendering = Rendering { modifier, onClose -> UnlockCircleScreen(modifier, onClose) }
    ),
    PUSHED_FOOTER(
        label = "Pushed Footer",
        rendering = Rendering { modifier, onClose -> PushedFooterScreen(modifier, onClose) }
    ),
    SHAKER(
        label = "Shaker",
        rendering = Rendering { modifier, onClose -> ShakerScreen(modifier, onClose) }
    ),
    WAVE_BACKGROUND(
        label = "Wave Background",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                WAVE_BACKGROUND.label,
                modifier,
                onClose
            ) { WavesBackground() }
        }
    ),
    PACMAN(
        label = "Pacman",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                PACMAN.label,
                modifier,
                onClose
            ) { Pacmans() }
        }
    ),
    BOTTOM_SHEET(
        label = "Bottom Sheet",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                BOTTOM_SHEET.label,
                modifier,
                onClose
            ) { BottomSheetScreen() }
        }
    ),
    BOTTOM_BAR(
        label = "Bottom Bar",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                BOTTOM_BAR.label,
                modifier,
                onClose
            ) { BottomBarScreen() }
        }
    ),
    SHADER(
        label = "Shader",
        rendering = Rendering { modifier, onClose ->
            ScreenWrapper(
                SHADER.label,
                modifier,
                onClose
            ) { ShaderScreen() }
        }
    )
}

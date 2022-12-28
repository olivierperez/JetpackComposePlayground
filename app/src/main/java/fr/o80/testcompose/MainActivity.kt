package fr.o80.testcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.o80.testcompose.screen.ScreenList
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val systemUiController = rememberSystemUiController(window)
            val useDarkIcons = !isSystemInDarkTheme()

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = false
                )

                onDispose {}
            }

            TestComposeCanvasTheme {
                App()
            }
        }
    }

    @Composable
    private fun App() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var screen: Screen? by rememberSaveable(key = "screen") {
                mutableStateOf(
                    null
                )
            }

            Render(
                screen,
                onScreenSelect = { screen = it }
            )

            BackHandler(
                enabled = screen != null,
                onBack = { screen = null }
            )
        }
    }

    @Composable
    private fun Render(
        screen: Screen?,
        onScreenSelect: (Screen?) -> Unit
    ) {
        when (screen) {
            null -> ScreenList(
                Modifier.fillMaxSize(),
                onScreenSelect = onScreenSelect
            )

            else -> screen.render(
                Modifier.fillMaxSize(),
                onClose = { onScreenSelect(null) }
            )
        }
    }
}

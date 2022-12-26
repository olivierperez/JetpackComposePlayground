package fr.o80.testcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestComposeCanvasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var screen: Screen? by rememberSaveable(key = "screen") { mutableStateOf(null) }

                    Render(
                        screen,
                        onScreenSelect = { screen = it }
                    )
                }
            }
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

            else -> screen.render(Modifier.fillMaxSize(), onClose = { onScreenSelect(null) })
        }
    }
}

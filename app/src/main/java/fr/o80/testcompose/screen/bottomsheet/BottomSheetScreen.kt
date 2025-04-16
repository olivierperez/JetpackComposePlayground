package fr.o80.testcompose.screen.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.screen.bottomsheet.component.BottomSheet
import fr.o80.testcompose.screen.bottomsheet.component.BottomSheetState
import fr.o80.testcompose.screen.bottomsheet.component.behavior.HeaderFooterFirstState
import fr.o80.testcompose.screen.bottomsheet.component.behavior.rememberHeaderFooterFirstBehavior
import fr.o80.testcompose.screen.bottomsheet.component.rememberBottomSheetState
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme
import kotlinx.coroutines.launch

@Composable
fun BottomSheetScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val state = rememberBottomSheetState(
            behavior = rememberHeaderFooterFirstBehavior(HeaderFooterFirstState.Header),
            onStateChanged = { println("On state changed: $it") }
        )

        ControlButtons(
            state,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        BottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = state,
            header = {
                Text(
                    "Un super titre",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                )
            },
            content = {
                Text(
                    "Puis un texte normal sans pression, t'as vu ?\nEt ça peut\nse trouve sur plusieurs lignes\npour prendre beaucoup de place.\nNon ?",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.inverseSurface)
                )
            },
            footer = {
                Text(
                    "Footer",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                )
            }
        )
    }
}

@Composable
private fun ControlButtons(
    state: BottomSheetState<HeaderFooterFirstState>,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val disableColor = MaterialTheme.colorScheme.background
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier.drawWithContent {
                drawContent()
                val disableSize = size.width * state.progressBetween(
                    HeaderFooterFirstState.HeaderFooter,
                    HeaderFooterFirstState.Header
                )
                drawRect(
                    color = disableColor.copy(alpha = .9f),
                    size = Size(
                        width = disableSize,
                        height = size.height
                    )
                )
            },
            onClick = {
                scope.launch { state.scrollTo(HeaderFooterFirstState.Header) }
            }
        ) {
            Text("Close")
        }
        Button(onClick = {
            scope.launch { state.scrollTo(HeaderFooterFirstState.HeaderFooter) }
        }) {
            Text("Half")
        }
        Button(
            modifier = Modifier.drawWithContent {
                drawContent()
                val disableSize = size.width * state.progressBetween(
                    HeaderFooterFirstState.HeaderFooter,
                    HeaderFooterFirstState.Full
                )
                drawRect(
                    color = disableColor.copy(alpha = .9f),
                    topLeft = Offset(size.width - disableSize, 0f),
                    size = Size(disableSize, size.height)
                )
            },
            onClick = {
                scope.launch { state.scrollTo(HeaderFooterFirstState.Full) }
            }
        ) {
            Text("Open")
        }
    }
}

@Preview
@Composable
private fun PageWithBottomSheetPreview() {
    TestComposeCanvasTheme {
        BottomSheetScreen()
    }
}

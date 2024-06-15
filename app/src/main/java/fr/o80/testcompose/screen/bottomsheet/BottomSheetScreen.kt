package fr.o80.testcompose.screen.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme

@Composable
fun BottomSheetScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        val behavior = rememberHeaderFooterFirstBehavior(HeaderFooterFirsState.Header)
        BottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            behavior = behavior,
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

@Preview
@Composable
private fun PageWithBottomSheetPreview() {
    TestComposeCanvasTheme {
        BottomSheetScreen()
    }
}

package fr.o80.testcompose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.Screen
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme

@Composable
fun ScreenList(
    modifier: Modifier,
    onScreenSelect: (Screen) -> Unit
) {
    LazyColumn(modifier = modifier.systemBarsPadding()) {
        item {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Demos",
                style = MaterialTheme.typography.displaySmall,
            )
        }
        items(Screen.values()) { screen ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onScreenSelect(screen) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = screen.label
                )
                Icon(Icons.AutoMirrored.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
fun ScreenListPreview() {
    TestComposeCanvasTheme {
        ScreenList(
            modifier = Modifier.fillMaxSize(),
            onScreenSelect = {}
        )
    }
}

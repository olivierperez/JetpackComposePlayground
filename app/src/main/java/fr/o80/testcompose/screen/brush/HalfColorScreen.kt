package fr.o80.testcompose.screen.brush

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HalfColorScreen(modifier: Modifier = Modifier) {
    Box(modifier
        .fillMaxSize()
        .drawBehind {
            drawRect(HalfColorBrush(startColor = Color.Black, endColor = Color.White))
        }
    )
}

@Preview
@Composable
private fun HalfColorScreenPreview() {
    HalfColorScreen(
        modifier = Modifier.fillMaxSize()
    )
}
package fr.o80.testcompose.screen.wave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme

private const val cardTitle = "Une super carte"
private const val cardText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."

@Composable
fun WavesBackground(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .wavesBackground(
                    colors = listOf(
                        Color(0xffFFA000),
                        Color(0xffFFCA28)
                    ),
                    waveColors = Color.Black.copy(alpha = .15f),
                    intensity1 = .015f,
                    intensity2 = .03f,
                    iterations = 1.6f
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides Color.Black) {
                Text(text = cardTitle, style = MaterialTheme.typography.headlineMedium)
                Text(text = cardText)
            }
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .wavesBackground(
                    colors = listOf(
                        Color(0xff4A148C),
                        Color(0xff7B1FA2)
                    ),
                    waveColors = Color.White.copy(alpha = .1f),
                    intensity1 = .05f,
                    intensity2 = .06f,
                    iterations = 2.2f
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides Color.White) {
                Text(text = cardTitle, style = MaterialTheme.typography.headlineMedium)
                Text(text = cardText)
            }
        }
    }
}

@Preview
@Composable
private fun WaveBackgroundPreview() {
    TestComposeCanvasTheme {
        WavesBackground()
    }
}

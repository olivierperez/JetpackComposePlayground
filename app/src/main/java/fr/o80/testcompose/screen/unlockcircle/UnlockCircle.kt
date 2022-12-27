package fr.o80.testcompose.screen.unlockcircle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme
import fr.o80.testcompose.ui.theme.component.CloseIcon
import kotlin.random.Random

@Composable
fun UnlockCircle(
    modifier: Modifier,
    onClose: () -> Unit
) {
    Box(modifier) {
        var finished by remember { mutableStateOf(false) }
        if (finished) {
            Text(
                text = "You won!",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            val degres = remember { listOf(1, 2, 3).map { Random.nextFloat() * 360 } }
            Lock(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
                lockDegres = degres,
                onFullyUnlocked = { finished = true }
            )
        }
        CloseIcon(onClick = onClose)
    }
}

@Preview
@Composable
fun UnlockCirclePreview() {
    TestComposeCanvasTheme {
        UnlockCircle(
            Modifier.fillMaxSize(),
            onClose = {}
        )
    }
}
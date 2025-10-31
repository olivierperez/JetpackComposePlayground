package fr.o80.testcompose.screen.async

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Composable
fun AsyncContentLoaderScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Efficient()
        // NotEfficient()
    }
}

@Composable
fun Efficient() {
    val longTextState = rememberLongTextState()
    longTextState.content?.let {
        Text(
            text = it
        )
    }
}

@Composable
private fun NotEfficient() {
    val longContent = runBlocking {
        delay(5000)
        "Long Content"
    }
    Text(
        text = longContent
    )
}
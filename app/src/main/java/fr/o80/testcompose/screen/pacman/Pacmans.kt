package fr.o80.testcompose.screen.pacman

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Pacmans(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        PacmanCanvas(Modifier.fillMaxWidth())
    }
}

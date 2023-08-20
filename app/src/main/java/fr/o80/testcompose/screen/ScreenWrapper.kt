package fr.o80.testcompose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import fr.o80.testcompose.ui.theme.component.CloseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWrapper(
    label: String,
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier.graphicsLayer { this.alpha = alpha },
                title = { Text(label) },
                navigationIcon = { CloseIcon(onClick = onClose) },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            content = content
        )
    }
}

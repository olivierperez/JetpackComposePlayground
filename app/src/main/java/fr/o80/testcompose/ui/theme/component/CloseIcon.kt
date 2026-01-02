package fr.o80.testcompose.ui.theme.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.o80.testcompose.ui.icon.Close
import fr.o80.testcompose.ui.icon.Icons

@Composable
fun CloseIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors()
) {
    IconButton(onClick = onClick, modifier = modifier, colors = colors) {
        Icon(Icons.Close, contentDescription = "Close")
    }
}
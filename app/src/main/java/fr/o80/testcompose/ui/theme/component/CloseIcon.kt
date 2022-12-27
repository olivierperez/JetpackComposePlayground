package fr.o80.testcompose.ui.theme.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CloseIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(64.dp)) {
        Icon(Icons.Default.Close, contentDescription = "Close")
    }
}
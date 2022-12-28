package fr.o80.testcompose.screen.pushedfooter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.component.CloseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PushedFooterScreen(
    modifier: Modifier,
    onClose: () -> Unit
) {
    val listItems by remember { mutableStateOf(ListItems()) }

    LazyColumn(
        modifier = modifier.systemBarsPadding(),
        verticalArrangement = HeaderFooterArrangement
    ) {
        item(contentType = "Header") {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Pushed footer") },
                navigationIcon = { CloseIcon(onClose) },
            )
        }
        items(listItems.items, contentType = { "simple" }) { item ->
            Text(
                text = "Number $item",
                modifier = Modifier.padding(8.dp)
            )
        }
        item(contentType = "footer") {
            Text(
                text = "Footer",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

object HeaderFooterArrangement : Arrangement.Vertical {
    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray
    ) {
        var y = 0
        sizes.forEachIndexed { index, size ->
            outPositions[index] = y
            y += size
        }

        if (y < totalSize) {
            val lastIndex = outPositions.lastIndex
            outPositions[lastIndex] = totalSize - sizes.last()
        }
    }

}

private data class ListItems(
    val items: List<Int> = (1..10).toList()
)


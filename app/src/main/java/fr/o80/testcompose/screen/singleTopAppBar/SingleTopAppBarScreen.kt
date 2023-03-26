package fr.o80.testcompose.screen.singleTopAppBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.component.CloseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTopAppBarScreen(
    modifier: Modifier,
    onClose: () -> Unit
) {
    val listItems by remember { mutableStateOf(ListItems()) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SingleTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary),
                title = "This is awesome!",
                titleColor = MaterialTheme.colorScheme.onSecondary,
                maxHeight = 200.dp,
                navigationIcon = {
                    CloseIcon(
                        modifier = it,
                        onClick = onClose,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            items(listItems.items) { item ->
                Text(
                    text = "Number $item",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

private data class ListItems(
    val items: List<Int> = (1..100).toList()
)

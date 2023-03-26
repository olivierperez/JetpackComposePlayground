package fr.o80.testcompose.screen.twoTopAppBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.R
import fr.o80.testcompose.ui.theme.component.CloseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoTopAppBarScreen(
    modifier: Modifier,
    onClose: () -> Unit
) {
    val listItems by remember { mutableStateOf(ListItems()) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TwoTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                collapsedContent = { modifier ->
                    TopAppBar(
                        modifier = modifier,
                        title = { Text("Small Top App Bar") },
                        navigationIcon = { CloseIcon(onClick = onClose) }
                    )
                },
                expandedContent = { modifier ->
                    Box(modifier) {
                        Image(
                            painter = painterResource(R.drawable.background),
                            contentDescription = null,
                            colorFilter = ColorFilter.lighting(Color(0xFF808080), Color.Black),
                            contentScale = ContentScale.Crop
                        )
                        TopAppBar(
                            modifier = Modifier.fillMaxWidth(),
                            title = {},
                            navigationIcon = { CloseIcon(onClick = onClose) },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            text = "Large Top App Bar",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
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

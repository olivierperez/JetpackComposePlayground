package fr.o80.testcompose.screen.appearingtopbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.component.CloseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearingTopBar(
    modifier: Modifier,
    onClose: () -> Unit
) {
    val data: ListItems by remember { mutableStateOf(ListItems()) }

    val state = rememberLazyListState()

    val delta = with(LocalDensity.current) { 25.dp.toPx() }

    val targetAlpha by remember {
        derivedStateOf {
            when {
                state.firstVisibleItemIndex > 0 -> 1f
                state.firstVisibleItemScrollOffset > delta -> 1f
                else -> 0f
            }
        }
    }

    val alpha by animateFloatAsState(targetValue = targetAlpha, animationSpec = tween())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier.graphicsLayer { this.alpha = alpha },
                title = { Text("Appearing top bar") },
                navigationIcon = { CloseIcon(onClick = onClose) },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = innerPadding.calculateBottomPadding()
                ),
            state = state
        ) {
            items(data.items) { item ->
                Text(
                    text = "Number $item",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

private data class ListItems(
    val items: List<Int> = (1..100).toList()
)
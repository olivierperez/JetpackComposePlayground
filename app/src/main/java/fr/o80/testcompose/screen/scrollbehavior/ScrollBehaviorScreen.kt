package fr.o80.testcompose.screen.scrollbehavior

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollBehaviorScreen(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = remember { ScrollBehavior() }
    var headingHeight by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(headingHeight) {
        scrollBehavior.headerHeight = headingHeight
    }

    Box(
        modifier = modifier
    ) {
        Text(
            text = "Heading",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .onSizeChanged { headingHeight = it.height.toFloat() }
                .offset { IntOffset(0, scrollBehavior.remaining.toInt() - headingHeight.toInt()) }
                .background(Color.Cyan)
                .padding(16.dp)
                .fillMaxWidth()
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .offset { IntOffset(0, scrollBehavior.remaining.toInt()) }
                .nestedScroll(scrollBehavior)
                .fillMaxSize(),
        ) {
            items(50) {
                Text(
                    text = "Item $it",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .padding(16.dp)
                )
            }
        }
    }
}

private class ScrollBehavior : NestedScrollConnection {
    var headerHeight: Float = 500f
        set(value) {
            field = value
            remaining = value
        }
    var remaining by mutableFloatStateOf(headerHeight)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        Log.d("OPZ", "onPreScroll: available $available, remaining: $remaining")
        val newRemaining = (remaining + available.y).coerceIn(0f, headerHeight)
        val consumed = newRemaining - remaining
        Log.d("OPZ", "newRemaining: $newRemaining, consumed: $consumed")
        remaining = newRemaining
        return Offset(0f, consumed)
    }
}

@Preview
@Composable
private fun ScrollBehaviorScreenPreview() {
    TestComposeCanvasTheme {
        ScrollBehaviorScreen()
    }
}

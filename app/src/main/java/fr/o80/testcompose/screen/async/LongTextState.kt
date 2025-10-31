package fr.o80.testcompose.screen.async

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Stable
interface LongTextState {
    val content: String?
}

class LongTextStateImpl : LongTextState {
    override var content: String? by mutableStateOf("loading...")

    suspend fun load() {
        delay(2000)
        content = "Long Content"
        repeat(3) {
            delay(300)
            content += "."
        }
    }
}

@Composable
fun rememberLongTextState(): LongTextState {
    val state = remember { LongTextStateImpl() }
    LaunchedEffect(state) {
        state.load()
    }
    return state
}
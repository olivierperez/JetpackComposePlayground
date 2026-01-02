package fr.o80.testcompose.screen.bottombar

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.ui.icon.Add
import fr.o80.testcompose.ui.icon.Build
import fr.o80.testcompose.ui.icon.DateRange
import fr.o80.testcompose.ui.icon.Icons
import fr.o80.testcompose.ui.icon.Person
import fr.o80.testcompose.ui.icon.Star

@Composable
fun BottomBarScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        topBar = { Spacer(Modifier) },
        bottomBar = {
            BottomBar(
                modifier = Modifier
                    .fillMaxWidth(),
                fab = {
                    FloatingActionButton(onClick = { toast("Floating Action Button") }) {
                        Icon(Icons.Add, contentDescription = null)
                    }
                },
                fabPadding = 0.dp,
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
            ) {
                IconButton(onClick = { toast("Calendar") }) {
                    Icon(Icons.DateRange, contentDescription = null)
                }
                IconButton(onClick = { toast("Build") }) {
                    Icon(Icons.Build, contentDescription = null)
                }
                IconButton(onClick = { toast("Favorites") }) {
                    Icon(Icons.Star, contentDescription = null)
                }
                IconButton(onClick = { toast("Person") }) {
                    Icon(Icons.Person, contentDescription = null)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "Some content",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

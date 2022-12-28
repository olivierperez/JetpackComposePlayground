package fr.o80.testcompose.screen.customtabbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.R
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme
import fr.o80.testcompose.ui.theme.component.CloseIcon

@ExperimentalMaterial3Api
@Composable
fun CustomTopTabBar(
    modifier: Modifier,
    maxHeight: Dp = 250.dp,
    collapsedContent: @Composable (modifier: Modifier) -> Unit,
    onClose: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val localDensity = LocalDensity.current
    var smallHeight by remember { mutableStateOf(0f) }
    val maxHeightPx = with(localDensity) { maxHeight.toPx() }

    LaunchedEffect(smallHeight) {
        if (scrollBehavior?.state?.heightOffsetLimit != smallHeight - maxHeightPx) {
            scrollBehavior?.state?.heightOffsetLimit = smallHeight - maxHeightPx
        }
    }

    val colorTransitionFraction by remember {
        derivedStateOf { scrollBehavior?.state?.collapsedFraction ?: 0f }
    }
    val smallBarAlpha by remember { derivedStateOf { colorTransitionFraction } }
    val largeBarAlpha by remember { derivedStateOf { 1f - smallBarAlpha } }

    val largeHeightPx by remember {
        derivedStateOf {
            maxHeightPx + (scrollBehavior?.state?.heightOffset ?: 0f)
        }
    }

    Box(modifier) {
        LargeCustomTopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            barAlpha = { largeBarAlpha },
            heightPx = { largeHeightPx },
            onClose = onClose
        )
        collapsedContent(
            Modifier
                .graphicsLayer { this.alpha = smallBarAlpha }
                .onGloballyPositioned {
                    smallHeight = with(localDensity) {
                        it.size.height
                            .toDp()
                            .toPx()
                    }
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeCustomTopAppBar(
    modifier: Modifier,
    barAlpha: () -> Float,
    heightPx: () -> Float,
    onClose: () -> Unit
) {
    Box(
        modifier = modifier
            .graphicsLayer { this.alpha = barAlpha() }
            .maxHeight(heightPx)
    ) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomTopTabBarPreview() {
    TestComposeCanvasTheme {
        CustomTopTabBar(
            modifier = Modifier.fillMaxWidth(),
            collapsedContent = { modifier -> Text("Collapsed", modifier) },
            onClose = {}
        )
    }
}

@Preview
@Composable
fun LargeCustomTopTabBarPreview() {
    TestComposeCanvasTheme {
        LargeCustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            barAlpha = { 1f },
            heightPx = { 700f },
            onClose = {}
        )
    }
}
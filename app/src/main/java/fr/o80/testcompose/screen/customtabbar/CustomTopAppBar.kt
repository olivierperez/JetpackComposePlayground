package fr.o80.testcompose.screen.customtabbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.o80.testcompose.R
import fr.o80.testcompose.ui.theme.TestComposeCanvasTheme
import fr.o80.testcompose.ui.theme.component.CloseIcon

@ExperimentalMaterial3Api
@Composable
fun CustomTopTabBar(
    modifier: Modifier,
    maxHeight: Float = 700f,
    onClose: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val localDensity = LocalDensity.current
    var smallHeight by remember { mutableStateOf(0f) }

    LaunchedEffect(smallHeight) {
        if (scrollBehavior?.state?.heightOffsetLimit != smallHeight - maxHeight) {
            scrollBehavior?.state?.heightOffsetLimit = smallHeight - maxHeight
        }
    }

    val colorTransitionFraction = scrollBehavior?.state?.collapsedFraction ?: 0f
    val smallBarAlpha = colorTransitionFraction
    val largeBarAlpha = 1f - smallBarAlpha

    val largeHeightPx = maxHeight + (scrollBehavior?.state?.heightOffset ?: 0f)

    Box(modifier) {
        LargeCustomTopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            barAlpha = largeBarAlpha,
            heightPx = largeHeightPx,
            onClose = onClose
        )
        SmallCustomTopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    smallHeight = with(localDensity) {
                        it.size.height
                            .toDp()
                            .toPx()
                    }
                },
            barAlpha = smallBarAlpha,
            onClose = onClose
        )
    }
}

@Composable
private fun SmallCustomTopAppBar(
    modifier: Modifier,
    barAlpha: Float,
    onClose: () -> Unit
) {
    Row(
        modifier = modifier.alpha(barAlpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CloseIcon(onClose)
        Text(
            text = "Small Top App Bar",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun LargeCustomTopAppBar(
    modifier: Modifier,
    barAlpha: Float,
    heightPx: Float,
    onClose: () -> Unit
) {
    val height = with(LocalDensity.current) {
        heightPx.toDp()
    }

    Box(
        modifier = modifier
            .alpha(barAlpha)
            .height(height)
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            colorFilter = ColorFilter.lighting(Color(0xFF808080), Color.Black),
            contentScale = ContentScale.Crop
        )
        CloseIcon(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onClose
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
            onClose = {}
        )
    }
}

@Preview
@Composable
fun SmallCustomTopTabBarPreview() {
    TestComposeCanvasTheme {
        SmallCustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            onClose = {},
            barAlpha = 1f
        )
    }
}

@Preview
@Composable
fun LargeCustomTopTabBarPreview() {
    TestComposeCanvasTheme {
        LargeCustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            barAlpha = 1f,
            heightPx = 700f,
            onClose = {}
        )
    }
}
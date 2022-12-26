package fr.o80.testcompose.screen.unlockcircle

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class LockTheme(
    val lockColor: Color = Color(0xFFFF0000),
    val backgroundLockColor: Color = Color(0x22FF0000),
    val keyColor: Color = Color(0xFFFFC107),
    val strokeWidth: Float = 5f,
    val lockRadius: Float = 25f,
    val keyRadius: Float = 15f,
    val margin: Float = 10f,
)

val LocalLockTheme = staticCompositionLocalOf { LockTheme() }
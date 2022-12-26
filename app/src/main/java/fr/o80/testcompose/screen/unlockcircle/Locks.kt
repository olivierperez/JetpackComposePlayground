package fr.o80.testcompose.screen.unlockcircle

import androidx.compose.runtime.Immutable

@Immutable
data class Locks(
    val locks: List<Float>
) {
    fun withUnlocked(value: Float): Locks {
        return Locks(locks.filterNot { StrictMath.abs(it - value) < 10 })
    }

    fun isFullyUnlocked(): Boolean {
        return locks.isEmpty()
    }
}

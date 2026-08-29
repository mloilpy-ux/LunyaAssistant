package com.lunya.assistant.core

import android.util.Log
import kotlinx.coroutines.*

/**
 * Central decision brain of Lunya (from original / v7 / v8 projects).
 */
class LunyaBrain {

    companion object {
        private const val TAG = "LunyaBrain"
    }

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    var currentEmotion: EmotionState = EmotionState.IDLE_BORED
        private set

    var stats = CharacterStats()

    fun onUserInteraction(type: String) {
        Log.d(TAG, "Interaction: $type")
        when (type) {
            "pet", "tap" -> {
                stats.affection = (stats.affection + 0.05f).coerceAtMost(1f)
                currentEmotion = EmotionState.SURPRISED
            }
            "drink" -> {
                stats.energy = (stats.energy + 0.3f).coerceAtMost(1f)
                currentEmotion = EmotionState.DRINKING_ENERGY
            }
            "overclock" -> {
                stats.isOverclocked = true
                stats.speedMultiplier = 2.0f
                currentEmotion = EmotionState.OVERCLOCKED
            }
            else -> currentEmotion = EmotionState.IDLE_BORED
        }
    }

    fun tick() {
        // natural decay
        stats.energy = (stats.energy - 0.002f).coerceAtLeast(0f)
        if (stats.energy < 0.2f) currentEmotion = EmotionState.SLEEPING
    }

    fun shutdown() {
        scope.cancel()
    }
}

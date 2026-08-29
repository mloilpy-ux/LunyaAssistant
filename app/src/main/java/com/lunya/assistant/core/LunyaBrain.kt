package com.lunya.assistant.core

import android.util.Log
import com.lunya.assistant.animation.Emotion
import kotlinx.coroutines.*

/**
 * Central brain — stats + emotion hints used by overlay / hub.
 */
class LunyaBrain {

    companion object {
        private const val TAG = "LunyaBrain"
    }

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    var currentEmotion: Emotion = Emotion.IDLE
        private set

    var stats = CharacterStats()

    fun onUserInteraction(type: String) {
        Log.d(TAG, "Interaction: $type")
        when (type) {
            "pet", "tap" -> {
                stats.affection = (stats.affection + 0.05f).coerceAtMost(1f)
                currentEmotion = if (type == "pet") Emotion.LOVE else Emotion.CURIOUS
            }
            "drink" -> {
                stats.energy = (stats.energy + 0.3f).coerceAtMost(1f)
                currentEmotion = Emotion.DRINKING
            }
            "overclock" -> {
                stats.isOverclocked = true
                stats.speedMultiplier = 2.0f
                currentEmotion = Emotion.OVERCLOCKED
            }
            else -> currentEmotion = Emotion.IDLE
        }
    }

    fun tick() {
        stats.energy = (stats.energy - 0.002f).coerceAtLeast(0f)
        if (stats.energy < 0.2f) currentEmotion = Emotion.SLEEPY
    }

    fun shutdown() {
        scope.cancel()
    }
}

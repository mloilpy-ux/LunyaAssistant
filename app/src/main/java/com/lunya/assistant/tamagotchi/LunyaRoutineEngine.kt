package com.lunya.assistant.tamagotchi

import android.util.Log
import kotlinx.coroutines.*

/**
 * Tamagotchi-style daily routines for Lunya.
 */
class LunyaRoutineEngine {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun startDailyLoop() {
        scope.launch {
            while (isActive) {
                performRoutineTick()
                delay(60_000) // every minute
            }
        }
        Log.d("RoutineEngine", "Daily loop started")
    }

    private fun performRoutineTick() {
        // Hunger, energy, affection decay/recovery logic
        Log.d("RoutineEngine", "Routine tick")
    }

    fun stop() {
        scope.cancel()
    }
}

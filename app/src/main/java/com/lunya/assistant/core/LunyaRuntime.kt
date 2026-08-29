package com.lunya.assistant.core

import android.content.Context
import android.content.SharedPreferences
import com.lunya.assistant.ai.LunyaReactionEngine
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean

/** Single runtime coordinator: preserves old modules while giving the character one persistent state. */
class LunyaRuntime(context: Context, apiKey: String) {
    private val app = context.applicationContext
    private val prefs: SharedPreferences = app.getSharedPreferences("lunya_runtime", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val busy = AtomicBoolean(false)
    private val brain = LunyaBrain()
    private val reactions = LunyaReactionEngine(app, apiKey)

    fun start() {
        if (prefs.getBoolean("runtime_started", false)) return
        prefs.edit().putBoolean("runtime_started", true).apply()
        scope.launch {
            while (isActive) {
                brain.tick()
                delay(15_000)
            }
        }
        event("entry")
    }

    fun event(type: String) {
        brain.onUserInteraction(type)
        if (!busy.compareAndSet(false, true)) return
        scope.launch(Dispatchers.IO) {
            try { reactions.reaction(type) } catch (_: Throwable) { /* local character remains functional offline */ }
            finally { busy.set(false) }
        }
    }

    fun stop() {
        prefs.edit().putBoolean("runtime_started", false).apply()
        scope.cancel()
    }

    fun currentOutfit() = reactions.currentOutfit()
    fun nextOutfit() = reactions.nextOutfit()
}

package com.lunya.assistant.core

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import com.lunya.assistant.generator.InfiniteDialogueSynthesisMatrix

/**
 * Handles touch / drag / gesture interactions with the floating avatar.
 */
class LunyaInteractionController(
    private val context: Context,
    private val brain: LunyaBrain
) {

    companion object {
        private const val TAG = "LunyaInteract"
    }

    private var lastTapTime = 0L

    fun onTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val now = System.currentTimeMillis()
                if (now - lastTapTime < 300) {
                    // double tap
                    brain.onUserInteraction("pet")
                    Log.d(TAG, InfiniteDialogueSynthesisMatrix.synthesizeGreeting())
                }
                lastTapTime = now
            }
            MotionEvent.ACTION_MOVE -> {
                // drag handling is done by the overlay service
            }
            MotionEvent.ACTION_UP -> {
                brain.onUserInteraction("tap")
            }
        }
        return true
    }

    fun onLongPress() {
        brain.onUserInteraction("overclock")
    }
}

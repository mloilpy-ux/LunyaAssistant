package com.lunya.assistant.core

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.lunya.assistant.animation.Emotion
import com.lunya.assistant.animation.LunyaAnimationDirector
import com.lunya.assistant.generator.InfiniteDialogueSynthesisMatrix

/**
 * Full interaction controller: tap, double-tap, long-press, drag, pet.
 * Drives both brain stats and animation director reactions + emotions.
 */
class LunyaInteractionController(
    private val context: Context,
    private val brain: LunyaBrain,
    private val director: LunyaAnimationDirector
) {

    companion object {
        private const val TAG = "LunyaInteract"
    }

    private var lastTapTime = 0L
    private var isDragging = false

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent) = true

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            brain.onUserInteraction("tap")
            director.triggerTap()
            director.currentEmotion = Emotion.CURIOUS
            Log.d(TAG, InfiniteDialogueSynthesisMatrix.synthesizeGreeting())
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            brain.onUserInteraction("pet")
            director.triggerPet()
            director.currentEmotion = Emotion.HAPPY
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            brain.onUserInteraction("overclock")
            director.triggerLongPress()
            director.currentEmotion = Emotion.OVERCLOCKED
        }
    })

    fun onTouch(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> isDragging = false
            MotionEvent.ACTION_MOVE -> {
                if (event.historySize > 0) {
                    val dx = event.x - event.getHistoricalX(0)
                    val dy = event.y - event.getHistoricalY(0)
                    if (dx * dx + dy * dy > 16) {
                        isDragging = true
                        // gentle pet while dragging
                        if (System.currentTimeMillis() % 400 < 20) {
                            director.triggerPet()
                            director.currentEmotion = Emotion.LOVE
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isDragging) {
                    // handled by gesture detector
                }
            }
        }
        return true
    }

    fun onAudioBeat(strength: Float) {
        director.triggerAudioBeat(strength)
        if (strength > 0.6f) director.currentEmotion = Emotion.EXCITED
    }

    fun onNotification() {
        director.triggerNotification()
        director.currentEmotion = Emotion.SURPRISED
    }

    fun setEmotion(emotion: Emotion) {
        director.currentEmotion = emotion
    }
}

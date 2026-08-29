package com.lunya.assistant.interaction

import android.util.Log
import com.lunya.assistant.animation.Emotion
import com.lunya.assistant.animation.LunyaAnimationDirector
import com.lunya.assistant.generator.CharacterPOVDialogueEngine
import com.lunya.assistant.generator.InfiniteInteractionMatrix
import com.lunya.assistant.generator.InfiniteItemFactory
import com.lunya.assistant.generator.InfiniteMotionCatalog

/**
 * Single entry point for "millions" of interactions.
 * User event -> matrix resolve -> emotion + motion + POV line + particles.
 */
class MillionInteractionHub(
    private val director: LunyaAnimationDirector
) {

    companion object {
        private const val TAG = "MillionHub"
        val COMBOS = InfiniteInteractionMatrix.estimatedCombinations()
    }

    data class Feedback(
        val spoken: String,
        val thought: String,
        val emotion: String,
        val motionId: String,
        val particle: String
    )

    fun handle(action: InfiniteInteractionMatrix.UserAction, seed: Long = System.currentTimeMillis()): Feedback {
        val result = InfiniteInteractionMatrix.resolve(action, director.currentEmotion.name, seed)

        director.currentEmotion = try {
            Emotion.valueOf(result.emotion)
        } catch (_: Exception) {
            Emotion.IDLE
        }

        when (action) {
            InfiniteInteractionMatrix.UserAction.PET,
            InfiniteInteractionMatrix.UserAction.DOUBLE_TAP -> director.triggerPet()
            InfiniteInteractionMatrix.UserAction.TAP -> director.triggerTap()
            InfiniteInteractionMatrix.UserAction.LONG_PRESS -> director.triggerLongPress()
            InfiniteInteractionMatrix.UserAction.NOTIFICATION -> director.triggerNotification()
            else -> director.triggerTap()
        }

        Log.d(TAG, "[$action] ${result.phrase} | thought=${result.thought} | motion=${result.motionId}")
        Log.d(TAG, "Estimated unique combos available: $COMBOS")

        return Feedback(
            spoken = result.phrase,
            thought = result.thought,
            emotion = result.emotion,
            motionId = result.motionId,
            particle = result.particle
        )
    }

    fun giveProceduralItem(seed: Long = System.currentTimeMillis()): Feedback {
        val item = InfiniteItemFactory.generate(seed)
        val line = CharacterPOVDialogueEngine.reactToItem(item.name, seed)
        director.currentEmotion = try {
            Emotion.valueOf(item.emotionOnUse)
        } catch (_: Exception) {
            Emotion.HAPPY
        }
        director.triggerPet()
        return Feedback(line.spoken, line.thought, line.mood, "bounce", "sparkle")
    }

    fun morningLine(): Feedback {
        val line = CharacterPOVDialogueEngine.greet()
        director.currentEmotion = Emotion.HAPPY
        return Feedback(line.spoken, line.thought, line.mood, "arm_wave", "hearts")
    }

    fun nightLine(): Feedback {
        director.currentEmotion = Emotion.SLEEPY
        return Feedback(
            "Уже поздно... я посторожу твой сон.",
            CharacterPOVDialogueEngine.innerMonologue(),
            "SLEEPY",
            "sway",
            "zzz"
        )
    }
}

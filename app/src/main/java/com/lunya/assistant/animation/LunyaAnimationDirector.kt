package com.lunya.assistant.animation

import android.os.SystemClock

/**
 * Master animation director.
 * Blends idle + emotion + reaction layers every frame.
 * Produces a final pose map for ModularAvatarView / Overlay to render.
 *
 * This is how we get "millions of animations":
 * continuous procedural parameters × emotion × interaction × unique seed.
 */
class LunyaAnimationDirector(seed: Long = System.currentTimeMillis()) {

    private val idleLayer = IdleAnimationLayer(seed)
    private val emotionLayer = EmotionAnimationLayer()
    private val reactionLayer = InteractionReactionLayer()

    private val layers: List<AnimationLayer> = listOf(idleLayer, emotionLayer, reactionLayer)

    private var startMs = SystemClock.elapsedRealtime()
    private val finalPose = mutableMapOf<BodyPart, PartTransform>()

    var currentEmotion: Emotion
        get() = emotionLayer.emotion
        set(value) { emotionLayer.emotion = value }

    fun triggerPet() = reactionLayer.trigger(InteractionReactionLayer.ReactionType.PET)
    fun triggerTap() = reactionLayer.trigger(InteractionReactionLayer.ReactionType.TAP)
    fun triggerLongPress() = reactionLayer.trigger(InteractionReactionLayer.ReactionType.LONG_PRESS)
    fun triggerAudioBeat(strength: Float = 1f) =
        reactionLayer.trigger(InteractionReactionLayer.ReactionType.AUDIO_BEAT, strength)
    fun triggerNotification() =
        reactionLayer.trigger(InteractionReactionLayer.ReactionType.NOTIFICATION)

    /** Call every frame. Returns the blended pose for rendering. */
    fun evaluate(): Map<BodyPart, PartTransform> {
        val timeSec = (SystemClock.elapsedRealtime() - startMs) / 1000f

        // clear
        BodyPart.entries.forEach { part ->
            finalPose.getOrPut(part) { PartTransform() }.reset()
        }

        // evaluate layers (later layers can override / add)
        for (layer in layers) {
            if (!layer.enabled || layer.weight <= 0.001f) continue
            val layerPose = mutableMapOf<BodyPart, PartTransform>()
            layer.evaluate(timeSec, layerPose)

            layerPose.forEach { (part, tf) ->
                val dst = finalPose.getOrPut(part) { PartTransform() }
                val w = layer.weight
                // additive blend for offsets/rotation, multiplicative for scale
                dst.offsetX += tf.offsetX * w
                dst.offsetY += tf.offsetY * w
                dst.rotation += tf.rotation * w
                dst.scaleX *= (1f + (tf.scaleX - 1f) * w)
                dst.scaleY *= (1f + (tf.scaleY - 1f) * w)
                dst.alpha = dst.alpha * (1f - w) + tf.alpha * w
                dst.squash += tf.squash * w
            }
        }
        return finalPose
    }

    fun setEmotionFromName(name: String) {
        currentEmotion = try {
            Emotion.valueOf(name.uppercase())
        } catch (_: Exception) {
            Emotion.IDLE
        }
    }
}

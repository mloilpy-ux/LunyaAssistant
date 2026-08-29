package com.lunya.assistant.animation

/**
 * Reacts to user touches, drags, pets, long-press, audio beats etc.
 */
class InteractionReactionLayer : AnimationLayer {
    override val name = "reaction"
    override var weight = 0f
    override var enabled = true

    private var reactionTime = -100f
    private var reactionType = ReactionType.NONE
    private var reactionStrength = 1f

    enum class ReactionType { NONE, PET, TAP, DRAG, LONG_PRESS, AUDIO_BEAT, NOTIFICATION }

    fun trigger(type: ReactionType, strength: Float = 1f) {
        reactionType = type
        reactionStrength = strength.coerceIn(0.2f, 2f)
        reactionTime = 0f
        weight = 1f
    }

    override fun evaluate(timeSec: Float, pose: MutableMap<BodyPart, PartTransform>) {
        if (reactionType == ReactionType.NONE) {
            weight = 0f
            return
        }

        reactionTime += 0.016f // approx frame
        val life = reactionTime

        when (reactionType) {
            ReactionType.PET -> {
                pose[BodyPart.HEAD] = PartTransform(
                    offsetY = -6f * (1f - life).coerceAtLeast(0f),
                    rotation = ProceduralMotionLibrary.sine(life * 8f, 1f, 8f)
                )
                pose[BodyPart.EYES] = PartTransform(scaleY = 0.6f)
                pose[BodyPart.TAIL] = ProceduralMotionLibrary.tailWag(life * 3f, 1f)
                if (life > 1.2f) finish()
            }
            ReactionType.TAP -> {
                pose[BodyPart.TORSO] = PartTransform(squash = 0.12f * (1f - life * 2f).coerceAtLeast(0f))
                pose[BodyPart.HEAD] = PartTransform(offsetY = -4f * (1f - life * 3f).coerceAtLeast(0f))
                if (life > 0.4f) finish()
            }
            ReactionType.LONG_PRESS -> {
                pose[BodyPart.HEAD] = ProceduralMotionLibrary.overclockShake(life)
                pose[BodyPart.AURA] = PartTransform(scaleX = 1.3f, scaleY = 1.3f, alpha = 0.85f)
                if (life > 0.8f) finish()
            }
            ReactionType.AUDIO_BEAT -> {
                val s = reactionStrength * (1f - life * 4f).coerceAtLeast(0f)
                pose[BodyPart.TORSO] = PartTransform(scaleY = 1f + 0.08f * s, squash = -0.05f * s)
                pose[BodyPart.HEAD] = PartTransform(offsetY = -5f * s)
                if (life > 0.25f) finish()
            }
            ReactionType.NOTIFICATION -> {
                pose[BodyPart.EARS_LEFT] = PartTransform(rotation = -25f)
                pose[BodyPart.EARS_RIGHT] = PartTransform(rotation = 25f)
                pose[BodyPart.HEAD] = PartTransform(offsetY = -5f, rotation = 5f)
                if (life > 0.7f) finish()
            }
            else -> finish()
        }
    }

    private fun finish() {
        reactionType = ReactionType.NONE
        weight = 0f
    }
}

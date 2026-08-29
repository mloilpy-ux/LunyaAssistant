package com.lunya.assistant.animation

/**
 * Base idle layer — breathing, micro-twitches, unique seed variation.
 * Combined with emotion layer for infinite unique "millions" of poses.
 */
class IdleAnimationLayer(private val seed: Long = System.currentTimeMillis()) : AnimationLayer {
    override val name = "idle"
    override var weight = 1f
    override var enabled = true

    override fun evaluate(timeSec: Float, pose: MutableMap<BodyPart, PartTransform>) {
        val unique = ProceduralMotionLibrary.uniqueIdle(timeSec, seed)
        unique.forEach { (part, tf) ->
            val existing = pose.getOrPut(part) { PartTransform() }
            existing.offsetX += tf.offsetX * weight
            existing.offsetY += tf.offsetY * weight
            existing.rotation += tf.rotation * weight
            existing.scaleX *= (1f + (tf.scaleX - 1f) * weight)
            existing.scaleY *= (1f + (tf.scaleY - 1f) * weight)
        }

        // continuous micro ear + hair motion
        pose[BodyPart.EARS_LEFT] = ProceduralMotionLibrary.earTwitch(timeSec, -1f, 0.3f)
        pose[BodyPart.EARS_RIGHT] = ProceduralMotionLibrary.earTwitch(timeSec, 1f, 1.1f)
        pose[BodyPart.HAIR] = PartTransform(
            rotation = ProceduralMotionLibrary.sine(timeSec, 0.4f, 2.5f),
            offsetX = ProceduralMotionLibrary.sine(timeSec, 0.35f, 1.2f)
        )
        pose[BodyPart.ANTLERS] = PartTransform(
            rotation = ProceduralMotionLibrary.sine(timeSec, 0.28f, 1.8f)
        )
    }
}

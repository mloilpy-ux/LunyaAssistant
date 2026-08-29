package com.lunya.assistant.generator

import com.lunya.assistant.animation.BodyPart
import com.lunya.assistant.animation.PartTransform
import com.lunya.assistant.animation.ProceduralMotionLibrary
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Named motion clips built procedurally — infinite variants via seed + intensity.
 */
object InfiniteMotionCatalog {

    fun play(motionId: String, time: Float, intensity: Float = 1f, seed: Long = 0L): Map<BodyPart, PartTransform> {
        val i = intensity.coerceIn(0.2f, 2f)
        val rnd = Random(seed)
        return when (motionId) {
            "bounce" -> mapOf(
                BodyPart.TORSO to PartTransform(offsetY = -absWave(time, 5f) * 10f * i, squash = absWave(time, 5f) * 0.12f * i),
                BodyPart.HEAD to PartTransform(offsetY = -absWave(time, 5f) * 6f * i)
            )
            "shake" -> mapOf(
                BodyPart.HEAD to PartTransform(
                    offsetX = ProceduralMotionLibrary.sine(time, 20f, 3f * i),
                    rotation = ProceduralMotionLibrary.sine(time, 22f, 4f * i)
                )
            )
            "sway" -> mapOf(
                BodyPart.TORSO to PartTransform(rotation = ProceduralMotionLibrary.sine(time, 0.4f, 6f * i)),
                BodyPart.HEAD to PartTransform(rotation = ProceduralMotionLibrary.sine(time, 0.4f, 4f * i))
            )
            "ear_perk" -> mapOf(
                BodyPart.EARS_LEFT to PartTransform(rotation = -18f * i),
                BodyPart.EARS_RIGHT to PartTransform(rotation = 18f * i)
            )
            "ear_flat" -> mapOf(
                BodyPart.EARS_LEFT to PartTransform(rotation = 25f * i, offsetY = 4f),
                BodyPart.EARS_RIGHT to PartTransform(rotation = -25f * i, offsetY = 4f)
            )
            "look_away" -> mapOf(
                BodyPart.HEAD to PartTransform(rotation = -12f * i, offsetX = -5f * i),
                BodyPart.EYES to PartTransform(offsetX = -3f * i)
            )
            "lean_in" -> mapOf(
                BodyPart.TORSO to PartTransform(offsetY = -4f * i, scaleY = 1f + 0.03f * i),
                BodyPart.HEAD to PartTransform(offsetY = -8f * i)
            )
            "lean_back" -> mapOf(
                BodyPart.TORSO to PartTransform(offsetY = 3f * i),
                BodyPart.HEAD to PartTransform(offsetY = 5f * i, rotation = 5f * i)
            )
            "squash" -> mapOf(
                BodyPart.TORSO to PartTransform(squash = 0.2f * i, scaleX = 1f + 0.08f * i)
            )
            "stretch" -> mapOf(
                BodyPart.TORSO to PartTransform(squash = -0.15f * i, scaleY = 1f + 0.1f * i),
                BodyPart.ARM_LEFT to PartTransform(offsetY = -10f * i),
                BodyPart.ARM_RIGHT to PartTransform(offsetY = -10f * i)
            )
            "tail_wag" -> mapOf(
                BodyPart.TAIL to ProceduralMotionLibrary.tailWag(time, i)
            )
            "arm_wave" -> mapOf(
                BodyPart.ARM_RIGHT to PartTransform(
                    rotation = ProceduralMotionLibrary.sine(time, 3f, 35f * i),
                    offsetY = -5f * i
                )
            )
            "hide_face" -> mapOf(
                BodyPart.ARM_LEFT to PartTransform(offsetY = -20f * i, offsetX = 10f * i, rotation = 40f),
                BodyPart.ARM_RIGHT to PartTransform(offsetY = -20f * i, offsetX = -10f * i, rotation = -40f),
                BodyPart.HEAD to PartTransform(offsetY = 6f * i)
            )
            "peek" -> mapOf(
                BodyPart.HEAD to PartTransform(rotation = 15f * i, offsetX = 8f * i),
                BodyPart.EARS_LEFT to PartTransform(rotation = -10f)
            )
            "nod" -> mapOf(
                BodyPart.HEAD to PartTransform(offsetY = ProceduralMotionLibrary.sine(time, 2.5f, 5f * i))
            )
            "tilt" -> mapOf(
                BodyPart.HEAD to PartTransform(rotation = ProceduralMotionLibrary.sine(time, 0.5f, 12f * i))
            )
            "jump_tiny" -> mapOf(
                BodyPart.TORSO to PartTransform(offsetY = -absWave(time, 6f) * 14f * i),
                BodyPart.LEG_LEFT to PartTransform(offsetY = absWave(time, 6f) * 4f),
                BodyPart.LEG_RIGHT to PartTransform(offsetY = absWave(time, 6f) * 4f)
            )
            "vibrate" -> mapOf(
                BodyPart.HEAD to PartTransform(
                    offsetX = ProceduralMotionLibrary.sine(time, 28f, 1.5f * i),
                    offsetY = ProceduralMotionLibrary.sine(time, 31f, 1.2f * i)
                )
            )
            "glow_pulse" -> mapOf(
                BodyPart.AURA to PartTransform(
                    scaleX = 1f + ProceduralMotionLibrary.sine(time, 1.5f, 0.15f * i),
                    scaleY = 1f + ProceduralMotionLibrary.sine(time, 1.5f, 0.15f * i),
                    alpha = 0.5f + 0.4f * absWave(time, 1.5f)
                )
            )
            "sparkle" -> mapOf(
                BodyPart.AURA to PartTransform(alpha = 0.7f + rnd.nextFloat() * 0.3f),
                BodyPart.HEAD to PartTransform(offsetY = -2f * i)
            )
            else -> ProceduralMotionLibrary.uniqueIdle(time, seed)
        }
    }

    private fun absWave(t: Float, freq: Float): Float {
        val s = sin(t * freq * 2f * PI.toFloat())
        return if (s > 0f) s else -s
    }

    fun allMotionIds(): List<String> = listOf(
        "bounce", "shake", "sway", "ear_perk", "ear_flat", "look_away",
        "lean_in", "lean_back", "squash", "stretch", "tail_wag", "arm_wave",
        "hide_face", "peek", "nod", "tilt", "jump_tiny", "vibrate",
        "glow_pulse", "sparkle"
    )
}

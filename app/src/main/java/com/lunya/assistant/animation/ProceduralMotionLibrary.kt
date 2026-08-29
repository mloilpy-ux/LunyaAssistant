package com.lunya.assistant.animation

import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.abs
import kotlin.random.Random

/**
 * Infinite procedural motion primitives.
 * "Millions of animations" = continuous parameter space, not baked clips.
 */
object ProceduralMotionLibrary {

    fun sine(time: Float, freq: Float, amp: Float, phase: Float = 0f): Float =
        sin(time * freq * 2f * PI.toFloat() + phase) * amp

    fun breathe(time: Float, intensity: Float = 1f): PartTransform {
        val s = sine(time, 0.35f, 0.03f * intensity)
        return PartTransform(scaleX = 1f + s * 0.3f, scaleY = 1f + s, squash = s)
    }

    fun earTwitch(time: Float, side: Float, seed: Float): PartTransform {
        val burst = abs(sine(time * 3.1f + seed, 0.7f, 1f))
        val rot = if (burst > 0.92f) sine(time * 18f, 1f, 18f * side) else sine(time, 0.4f, 4f * side)
        return PartTransform(rotation = rot)
    }

    fun eyeBlink(time: Float): Float {
        // rare blink every ~3s
        val cycle = (time % 3.2f)
        return when {
            cycle < 0.08f -> 1f - (cycle / 0.04f).coerceIn(0f, 1f)  // close
            cycle < 0.16f -> (cycle - 0.08f) / 0.08f               // open
            else -> 1f
        }
    }

    fun headBob(time: Float, emotion: Emotion): PartTransform {
        val amp = 2.5f + emotion.energy * 4f
        return PartTransform(
            offsetY = sine(time, 0.5f + emotion.energy, amp),
            rotation = sine(time, 0.25f, 3f * emotion.energy)
        )
    }

    fun happyBounce(time: Float): PartTransform {
        val t = (time * 4f) % 1f
        val y = if (t < 0.5f) t * 2f else (1f - t) * 2f
        return PartTransform(offsetY = -y * 12f, squash = y * 0.15f)
    }

    fun tsundereLookAway(time: Float): PartTransform {
        return PartTransform(
            rotation = sine(time, 0.15f, 12f) - 8f,
            offsetX = sine(time, 0.2f, 4f)
        )
    }

    fun overclockShake(time: Float): PartTransform {
        return PartTransform(
            offsetX = sine(time, 22f, 2.5f),
            offsetY = sine(time, 19f, 1.8f),
            rotation = sine(time, 25f, 3f)
        )
    }

    fun sleepSway(time: Float): PartTransform {
        return PartTransform(
            offsetY = sine(time, 0.2f, 3f) + 6f,
            rotation = sine(time, 0.12f, 5f),
            scaleY = 0.97f + sine(time, 0.2f, 0.02f)
        )
    }

    fun armSwing(time: Float, side: Float, energy: Float): PartTransform {
        return PartTransform(
            rotation = sine(time, 0.6f + energy, 12f * side * (0.4f + energy)),
            offsetY = sine(time, 0.6f + energy, 3f * energy)
        )
    }

    fun tailWag(time: Float, mood: Float): PartTransform {
        return PartTransform(
            rotation = sine(time, 1.2f + mood * 2f, 25f + mood * 20f),
            offsetX = sine(time, 1.2f + mood * 2f, 4f)
        )
    }

    /** Generate a unique idle variation from seed (infinite unique idles). */
    fun uniqueIdle(time: Float, seed: Long): Map<BodyPart, PartTransform> {
        val rnd = Random(seed)
        val f1 = 0.2f + rnd.nextFloat() * 0.4f
        val f2 = 0.3f + rnd.nextFloat() * 0.5f
        val a1 = 1.5f + rnd.nextFloat() * 3f
        return mapOf(
            BodyPart.HEAD to PartTransform(
                offsetY = sine(time, f1, a1),
                rotation = sine(time, f2, a1 * 0.7f)
            ),
            BodyPart.EARS_LEFT to earTwitch(time, -1f, seed.toFloat()),
            BodyPart.EARS_RIGHT to earTwitch(time, 1f, seed * 1.3f),
            BodyPart.TORSO to breathe(time, 0.7f + rnd.nextFloat() * 0.5f)
        )
    }
}

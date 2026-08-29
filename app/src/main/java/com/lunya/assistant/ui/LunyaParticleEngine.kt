package com.lunya.assistant.ui

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.random.Random

/**
 * Simple particle / aura effects around the avatar.
 */
class LunyaParticleEngine {

    data class Particle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        var life: Float,
        var color: Int
    )

    private val particles = mutableListOf<Particle>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun emit(cx: Float, cy: Float, color: Int = 0xAA39FF14.toInt(), count: Int = 8) {
        repeat(count) {
            particles += Particle(
                x = cx,
                y = cy,
                vx = Random.nextFloat() * 4f - 2f,
                vy = Random.nextFloat() * -3f - 1f,
                life = 1f,
                color = color
            )
        }
    }

    fun update(dt: Float) {
        val it = particles.iterator()
        while (it.hasNext()) {
            val p = it.next()
            p.x += p.vx
            p.y += p.vy
            p.life -= dt * 0.8f
            if (p.life <= 0f) it.remove()
        }
    }

    fun draw(canvas: Canvas) {
        for (p in particles) {
            paint.color = p.color
            paint.alpha = (p.life * 255).toInt().coerceIn(0, 255)
            canvas.drawCircle(p.x, p.y, 4f * p.life, paint)
        }
    }
}

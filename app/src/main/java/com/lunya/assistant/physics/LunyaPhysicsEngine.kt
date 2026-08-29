package com.lunya.assistant.physics

import android.util.Log

/**
 * Simple physics for floating avatar (bounce, gravity, drag).
 */
class LunyaPhysicsEngine {

    companion object {
        private const val TAG = "LunyaPhysics"
    }

    var x = 0f
    var y = 0f
    var vx = 0f
    var vy = 0f

    private var gravity = 0.15f
    private var friction = 0.98f
    private var bounce = 0.6f

    fun update(dt: Float, screenW: Float, screenH: Float, avatarSize: Float) {
        vy += gravity * dt * 60f
        vx *= friction
        vy *= friction

        x += vx * dt * 60f
        y += vy * dt * 60f

        // walls
        if (x < 0) { x = 0f; vx = -vx * bounce }
        if (x + avatarSize > screenW) { x = screenW - avatarSize; vx = -vx * bounce }
        if (y < 0) { y = 0f; vy = -vy * bounce }
        if (y + avatarSize > screenH) { y = screenH - avatarSize; vy = -vy * bounce }
    }

    fun applyImpulse(ix: Float, iy: Float) {
        vx += ix
        vy += iy
        Log.d(TAG, "Impulse $ix,$iy -> vel $vx,$vy")
    }

    fun setPosition(nx: Float, ny: Float) {
        x = nx
        y = ny
        vx = 0f
        vy = 0f
    }
}

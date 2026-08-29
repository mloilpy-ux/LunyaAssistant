package com.lunya.assistant.animation

/**
 * Per-frame transform for a single body part.
 * All values are relative offsets / scales used by the renderer.
 */
data class PartTransform(
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
    var rotation: Float = 0f,   // degrees
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var alpha: Float = 1f,
    var squash: Float = 0f      // -1..1 vertical squash/stretch
) {
    fun reset() {
        offsetX = 0f; offsetY = 0f; rotation = 0f
        scaleX = 1f; scaleY = 1f; alpha = 1f; squash = 0f
    }

    fun lerpToward(target: PartTransform, t: Float) {
        offsetX += (target.offsetX - offsetX) * t
        offsetY += (target.offsetY - offsetY) * t
        rotation += (target.rotation - rotation) * t
        scaleX += (target.scaleX - scaleX) * t
        scaleY += (target.scaleY - scaleY) * t
        alpha += (target.alpha - alpha) * t
        squash += (target.squash - squash) * t
    }
}

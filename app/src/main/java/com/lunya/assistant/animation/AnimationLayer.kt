package com.lunya.assistant.animation

/**
 * One animation layer (idle breathing, emotion face, reaction, etc.).
 * Layers are blended by weight.
 */
interface AnimationLayer {
    val name: String
    var weight: Float          // 0..1
    var enabled: Boolean

    /** Write contribution into the shared pose map (additive or override). */
    fun evaluate(timeSec: Float, pose: MutableMap<BodyPart, PartTransform>)
}

package com.lunya.assistant.animation

/**
 * Rich emotion set that drives facial + body animation layers.
 */
enum class Emotion(
    val energy: Float,          // 0..1 how energetic
    val openness: Float,        // eyes/mouth openness bias
    val tension: Float,         // stiff vs loose
    val colorShift: Int         // aura tint hint
) {
    IDLE(0.2f, 0.4f, 0.2f, 0x5539FF14),
    HAPPY(0.7f, 0.8f, 0.1f, 0x88FFEB3B),
    TSUNDERE(0.5f, 0.3f, 0.7f, 0x77FF003F),
    SHY(0.3f, 0.2f, 0.5f, 0x66FFAAE6),
    SLEEPY(0.05f, 0.1f, 0.1f, 0x554A88B8),
    SURPRISED(0.9f, 1.0f, 0.8f, 0xAAFFFFFF),
    ANGRY(0.8f, 0.3f, 0.9f, 0xAAFF003F),
    LOVE(0.6f, 0.7f, 0.2f, 0x88FF80AB),
    OVERCLOCKED(1.0f, 0.9f, 0.4f, 0xAA39FF14),
    DRINKING(0.4f, 0.5f, 0.3f, 0x88FFEB3B),
    BORED(0.15f, 0.3f, 0.3f, 0x44666688),
    CURIOUS(0.5f, 0.7f, 0.3f, 0x7700E5FF),
    SAD(0.1f, 0.2f, 0.4f, 0x556B3FA0),
    EXCITED(0.95f, 0.9f, 0.2f, 0xAAFFEB3B),
    FOCUSED(0.4f, 0.5f, 0.6f, 0x5539FF14)
}

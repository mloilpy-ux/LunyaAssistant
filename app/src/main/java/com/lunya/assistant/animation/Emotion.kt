package com.lunya.assistant.animation

/**
 * Rich emotion set that drives facial + body animation layers.
 */
enum class Emotion(
    val energy: Float,
    val openness: Float,
    val tension: Float,
    val colorShift: Int
) {
    IDLE(0.2f, 0.4f, 0.2f, 0x5539FF14.toInt()),
    HAPPY(0.7f, 0.8f, 0.1f, 0x88FFEB3B.toInt()),
    TSUNDERE(0.5f, 0.3f, 0.7f, 0x77FF003F.toInt()),
    SHY(0.3f, 0.2f, 0.5f, 0x66FFAAE6.toInt()),
    SLEEPY(0.05f, 0.1f, 0.1f, 0x554A88B8.toInt()),
    SURPRISED(0.9f, 1.0f, 0.8f, 0xAAFFFFFF.toInt()),
    ANGRY(0.8f, 0.3f, 0.9f, 0xAAFF003F.toInt()),
    LOVE(0.6f, 0.7f, 0.2f, 0x88FF80AB.toInt()),
    OVERCLOCKED(1.0f, 0.9f, 0.4f, 0xAA39FF14.toInt()),
    DRINKING(0.4f, 0.5f, 0.3f, 0x88FFEB3B.toInt()),
    BORED(0.15f, 0.3f, 0.3f, 0x44666688.toInt()),
    CURIOUS(0.5f, 0.7f, 0.3f, 0x7700E5FF.toInt()),
    SAD(0.1f, 0.2f, 0.4f, 0x556B3FA0.toInt()),
    EXCITED(0.95f, 0.9f, 0.2f, 0xAAFFEB3B.toInt()),
    FOCUSED(0.4f, 0.5f, 0.6f, 0x5539FF14.toInt())
}

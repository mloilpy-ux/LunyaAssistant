package com.lunya.assistant.core

enum class EmotionState {
    IDLE_BORED,
    TSUNDERE_ANGRY,
    DRINKING_ENERGY,
    OVERCLOCKED,
    SLEEPING,
    SURPRISED,
    HANGING_EDGE
}

data class CharacterStats(
    var energy: Float = 0.6f,        // 0.0 .. 1.0
    var affection: Float = 0.3f,     // 0.0 .. 1.0 (Tsundere meter)
    var speedMultiplier: Float = 1.0f,
    var isOverclocked: Boolean = false
)

package com.lunya.assistant.core

data class CharacterStats(
    var energy: Float = 0.6f,
    var affection: Float = 0.3f,
    var speedMultiplier: Float = 1.0f,
    var isOverclocked: Boolean = false
)

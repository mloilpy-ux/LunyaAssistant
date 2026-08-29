package com.lunya.assistant.core

/** Central feature registry: one place for switches without deleting any existing module. */
data class LunyaFeatureFlags(
    val overlay: Boolean = true,
    val appAwareness: Boolean = true,
    val notifications: Boolean = true,
    val reactions: Boolean = true,
    val wardrobe: Boolean = true,
    val proactive: Boolean = true,
    val tamagotchi: Boolean = true,
    val voice: Boolean = true,
    val proceduralInteractions: Boolean = true,
    val physics: Boolean = true,
    val animationLayers: Boolean = true,
    val aiTools: Boolean = true
)

object LunyaFeatureRegistry {
    val defaults = LunyaFeatureFlags()
}

package com.lunya.assistant.core

import com.lunya.assistant.R

data class EnergyDrinkItem(
    val id: String,
    val name: String,
    val flavor: String,
    val iconRes: Int,
    val auraColor: Int,
    val speedBoost: Float,
    val durationMs: Long
)

object InventoryManager {
    val ITEMS = listOf(
        EnergyDrinkItem(
            id = "can_toxic_lime",
            name = "Toxic Lime",
            flavor = "Оригинальный кислый лайм",
            iconRes = R.drawable.ic_can_toxic_lime,
            auraColor = 0xAA39FF14.toInt(),
            speedBoost = 1.8f,
            durationMs = 60_000L
        ),
        EnergyDrinkItem(
            id = "can_glitch_red",
            name = "Glitch Red",
            flavor = "Багровый оверклок (Root)",
            iconRes = R.drawable.ic_can_glitch_red,
            auraColor = 0xAAFF003F.toInt(),
            speedBoost = 2.2f,
            durationMs = 90_000L
        ),
        EnergyDrinkItem(
            id = "can_ultra_white",
            name = "Ultra White",
            flavor = "Ледяной цитрус без сахара",
            iconRes = R.drawable.ic_can_ultra_white,
            auraColor = 0xAAFFFFFF.toInt(),
            speedBoost = 1.4f,
            durationMs = 45_000L
        ),
        EnergyDrinkItem(
            id = "plush_shark",
            name = "Cyber Shark",
            flavor = "Плюшевая акула для объятий",
            iconRes = R.drawable.ic_plush_shark,
            auraColor = 0x884A88B8.toInt(),
            speedBoost = 1.0f,
            durationMs = 30_000L
        )
    )
}

package com.lunya.assistant.core

import com.lunya.assistant.R

data class SkinPreset(
    val id: String,
    val title: String,
    val topClothesRes: Int,
    val hairRes: Int,
    val glassesRes: Int,
    val defaultAuraColor: Int
)

object WardrobeManager {
    val PRESETS = mapOf(
        "SKIN_TOXIC" to SkinPreset(
            id = "SKIN_TOXIC",
            title = "Toxic Street",
            topClothesRes = R.drawable.ic_outfit_toxic_hoodie,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = 0x5539FF14
        ),
        "SKIN_ROOT" to SkinPreset(
            id = "SKIN_ROOT",
            title = "Root Hacker",
            topClothesRes = R.drawable.ic_outfit_glitch_cloak,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = 0x77FF003F
        ),
        "SKIN_COZY" to SkinPreset(
            id = "SKIN_COZY",
            title = "Cozy Pastel",
            topClothesRes = R.drawable.ic_outfit_cozy_sweater,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = 0x44FFAAE6
        )
    )

    var currentSkin: SkinPreset = PRESETS["SKIN_TOXIC"]!!
}

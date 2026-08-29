package com.lunya.assistant.wardrobe

import com.lunya.assistant.R
import com.lunya.assistant.core.SkinPreset

/**
 * Mega catalog of all available skins including Nana Banana and new outfits.
 */
object MegaWardrobeCatalog {

    val ALL_SKINS = listOf(
        SkinPreset(
            id = "SKIN_TOXIC",
            title = "Toxic Street",
            topClothesRes = R.drawable.ic_outfit_toxic_hoodie,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = 0x5539FF14
        ),
        SkinPreset(
            id = "SKIN_ROOT",
            title = "Root Hacker",
            topClothesRes = R.drawable.ic_outfit_glitch_cloak,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = 0x77FF003F
        ),
        SkinPreset(
            id = "SKIN_COZY",
            title = "Cozy Pastel",
            topClothesRes = R.drawable.ic_outfit_cozy_sweater,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = 0x44FFAAE6
        ),
        SkinPreset(
            id = "SKIN_NANA_BANANA",
            title = "Nana Banana",
            topClothesRes = R.drawable.ic_outfit_nana_banana,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = 0x55FFEB3B
        )
    )

    fun getById(id: String): SkinPreset? = ALL_SKINS.find { it.id == id }
}

package com.lunya.assistant.generator

import android.util.Log
import com.lunya.assistant.R
import com.lunya.assistant.core.SkinPreset
import kotlin.random.Random

/**
 * Infinite procedural wardrobe generator (v8 Infinite).
 * Creates endless unique skin combinations.
 */
object InfiniteWardrobeMatrixGenerator {

    private val baseColors = listOf(
        0x5539FF14, 0x77FF003F, 0x44FFAAE6, 0x55FFEB3B, 0x554A88B8, 0x55FFFFFF
    )

    private val outfits = listOf(
        R.drawable.ic_outfit_toxic_hoodie,
        R.drawable.ic_outfit_glitch_cloak,
        R.drawable.ic_outfit_cozy_sweater,
        R.drawable.ic_outfit_nana_banana
    )

    fun generateRandomSkin(seed: Long = System.currentTimeMillis()): SkinPreset {
        val rnd = Random(seed)
        val id = "PROC_${seed.toString(16).takeLast(8)}"
        return SkinPreset(
            id = id,
            title = "Infinite #$id",
            topClothesRes = outfits[rnd.nextInt(outfits.size)],
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultAuraColor = baseColors[rnd.nextInt(baseColors.size)]
        ).also {
            Log.d("InfiniteWardrobe", "Generated ${it.title}")
        }
    }
}

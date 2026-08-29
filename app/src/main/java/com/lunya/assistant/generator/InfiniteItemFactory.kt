package com.lunya.assistant.generator

import com.lunya.assistant.R
import kotlin.random.Random

/**
 * Procedural item factory — generates unique item descriptors
 * from pools (names, effects, phrases). Millions of logical items
 * without millions of PNG/XML files.
 */
object InfiniteItemFactory {

    data class GeneratedItem(
        val id: String,
        val name: String,
        val description: String,
        val iconRes: Int,
        val category: String,
        val emotionOnUse: String,
        val phrase: String,
        val rarity: Int          // 1..5
    )

    private val prefixes = listOf(
        "Cyber", "Neon", "Glitch", "Pastel", "Toxic", "Frost", "Violet",
        "Banana", "Quantum", "Lo-Fi", "Midnight", "Holo", "Pixel", "Soft"
    )
    private val cores = listOf(
        "Can", "Plush", "Charm", "Badge", "Crystal", "Cartridge", "Chip",
        "Orb", "Badge", "Ticket", "Key", "Coin", "Sticker", "Capsule"
    )
    private val suffixes = listOf(
        "Mk.II", "Zero", "Plus", "EX", "∞", "Lite", "Max", "Dream", "Rush"
    )

    private val iconPool = listOf(
        R.drawable.ic_can_toxic_lime,
        R.drawable.ic_can_banana_nitro,
        R.drawable.ic_can_frost_blue,
        R.drawable.ic_can_violet_punch,
        R.drawable.ic_plush_shark,
        R.drawable.ic_item_boba_tea,
        R.drawable.ic_item_gamepad,
        R.drawable.ic_item_heart_plush,
        R.drawable.ic_item_star_sticker,
        R.drawable.ic_item_headphones,
        R.drawable.ic_flower_white_clip,
        R.drawable.ic_item_energy_bar
    )

    private val emotions = listOf(
        "HAPPY", "LOVE", "EXCITED", "SHY", "OVERCLOCKED", "CURIOUS", "TSUNDERE"
    )

    private val phrases = listOf(
        "Ого... это мне?", "Буду хранить.", "Ням!", "Стильно.",
        "Ты правда понял мой вкус.", "Ещё одно в коллекцию.~",
        "Не ожидала.", "Спасибо... бака."
    )

    fun generate(seed: Long = System.currentTimeMillis()): GeneratedItem {
        val rnd = Random(seed)
        val name = listOf(
            prefixes[rnd.nextInt(prefixes.size)],
            cores[rnd.nextInt(cores.size)],
            suffixes[rnd.nextInt(suffixes.size)]
        ).joinToString(" ")
        val id = "gen_${seed.toString(16).takeLast(8)}"
        return GeneratedItem(
            id = id,
            name = name,
            description = "Процедурный артефакт #$id из бесконечного инвентаря Луни",
            iconRes = iconPool[rnd.nextInt(iconPool.size)],
            category = listOf("DRINK", "PLUSH", "GADGET", "STICKER").random(rnd),
            emotionOnUse = emotions[rnd.nextInt(emotions.size)],
            phrase = phrases[rnd.nextInt(phrases.size)],
            rarity = 1 + rnd.nextInt(5)
        )
    }

    fun generateBatch(count: Int, baseSeed: Long = System.currentTimeMillis()): List<GeneratedItem> {
        return (0 until count).map { generate(baseSeed + it * 17_389L) }
    }
}

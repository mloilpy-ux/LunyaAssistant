package com.lunya.assistant.wardrobe

import android.content.Context

class LunyaWardrobe(context: Context) {
    private val prefs = context.getSharedPreferences("lunya_wardrobe", Context.MODE_PRIVATE)
    var currentIndex: Int
        get() = prefs.getInt("set_index", 0).coerceIn(0, MegaWardrobeCatalog.OUTFIT_SETS.lastIndex)
        private set(value) { prefs.edit().putInt("set_index", value).apply() }

    fun current(): OutfitSet = MegaWardrobeCatalog.OUTFIT_SETS[currentIndex]

    fun next(): OutfitSet {
        currentIndex = (currentIndex + 1) % MegaWardrobeCatalog.OUTFIT_SETS.size
        return current()
    }

    fun select(index: Int): OutfitSet {
        currentIndex = index.coerceIn(0, MegaWardrobeCatalog.OUTFIT_SETS.lastIndex)
        return current()
    }
}

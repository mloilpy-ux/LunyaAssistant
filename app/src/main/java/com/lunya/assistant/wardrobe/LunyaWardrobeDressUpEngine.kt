package com.lunya.assistant.wardrobe

import android.util.Log
import com.lunya.assistant.ui.ModularAvatarView

/**
 * Engine that applies outfit sets and individual items to the avatar.
 */
class LunyaWardrobeDressUpEngine(private val avatarView: ModularAvatarView) {

    companion object {
        private const val TAG = "DressUpEngine"
    }

    var currentSet: OutfitSet = MegaWardrobeCatalog.OUTFIT_SETS.first()
        private set

    fun wearSet(setId: String) {
        val set = MegaWardrobeCatalog.OUTFIT_SETS.find { it.setId == setId } ?: return
        currentSet = set
        avatarView.applyOutfit(set)
        Log.d(TAG, "Wearing ${set.setName}")
    }

    fun wearNext() {
        val idx = MegaWardrobeCatalog.OUTFIT_SETS.indexOf(currentSet)
        val next = MegaWardrobeCatalog.OUTFIT_SETS[(idx + 1) % MegaWardrobeCatalog.OUTFIT_SETS.size]
        wearSet(next.setId)
    }

    fun equipHandItem(itemId: String) {
        val item = MegaWardrobeCatalog.ALL_HAND_ITEMS.find { it.id == itemId } ?: return
        avatarView.setHandItem(item.iconRes)
        Log.d(TAG, "Equipped ${item.name}")
    }
}

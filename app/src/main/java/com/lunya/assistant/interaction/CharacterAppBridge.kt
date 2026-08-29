package com.lunya.assistant.interaction

import android.util.Log
import com.lunya.assistant.animation.Emotion
import com.lunya.assistant.animation.LunyaAnimationDirector
import com.lunya.assistant.core.CustomItem
import com.lunya.assistant.core.InventoryManager
import com.lunya.assistant.system.AppAwarenessEngine

/**
 * Bridges character reactions with apps and inventory items.
 * Gives Lunya things to "do" when user opens apps or gives items.
 */
class CharacterAppBridge(
    private val director: LunyaAnimationDirector,
    private val appAwareness: AppAwarenessEngine
) {

    companion object {
        private const val TAG = "CharAppBridge"
    }

    fun onItemUsed(itemId: String): String {
        val drink = InventoryManager.DRINKS.find { it.id == itemId }
        if (drink != null) {
            director.currentEmotion = try {
                Emotion.valueOf(drink.emotionHint)
            } catch (_: Exception) { Emotion.HAPPY }
            director.triggerPet()
            Log.d(TAG, "Drank ${drink.name}")
            return "Выпила ${drink.name}! Буст x${drink.speedBoost}"
        }

        val item = InventoryManager.CUSTOM_ITEMS.find { it.id == itemId }
        if (item != null) {
            when (item.category) {
                com.lunya.assistant.core.ItemCategory.PLUSH -> {
                    director.currentEmotion = Emotion.LOVE
                    director.triggerPet()
                }
                com.lunya.assistant.core.ItemCategory.FOOD, com.lunya.assistant.core.ItemCategory.DRINK -> {
                    director.currentEmotion = Emotion.HAPPY
                    director.triggerTap()
                }
                com.lunya.assistant.core.ItemCategory.GADGET -> {
                    director.currentEmotion = Emotion.EXCITED
                    director.triggerLongPress()
                }
                com.lunya.assistant.core.ItemCategory.STICKER -> {
                    director.currentEmotion = Emotion.SHY
                    director.triggerPet()
                }
                com.lunya.assistant.core.ItemCategory.REACTION -> {
                    when (item.id) {
                        "react_hearts" -> director.currentEmotion = Emotion.LOVE
                        "react_angry" -> director.currentEmotion = Emotion.TSUNDERE
                        "react_sleepy" -> director.currentEmotion = Emotion.SLEEPY
                    }
                    director.triggerTap()
                }
            }
            Log.d(TAG, "Used item ${item.name}: ${item.interactionPhrase}")
            return item.interactionPhrase
        }
        return "Хм? Что это?"
    }

    fun onAppOpened(packageName: String, label: String?) {
        appAwareness.onForegroundApp(packageName, label)
    }

    fun suggestActionForApp(packageName: String): String? {
        return appAwareness.getPhraseForCurrentApp()
    }
}

package com.lunya.assistant.core

import com.lunya.assistant.R

data class EnergyDrinkItem(
    val id: String,
    val name: String,
    val flavor: String,
    val iconRes: Int,
    val auraColor: Int,
    val speedBoost: Float,
    val durationMs: Long,
    val emotionHint: String = "HAPPY"
)

data class CustomItem(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: Int,
    val category: ItemCategory,
    val interactionPhrase: String
)

enum class ItemCategory { DRINK, FOOD, PLUSH, GADGET, STICKER, REACTION }

object InventoryManager {

    val DRINKS = listOf(
        EnergyDrinkItem("can_toxic_lime", "Toxic Lime", "Оригинальный кислый лайм", R.drawable.ic_can_toxic_lime, 0xAA39FF14.toInt(), 1.8f, 60_000L, "OVERCLOCKED"),
        EnergyDrinkItem("can_glitch_red", "Glitch Red", "Багровый оверклок", R.drawable.ic_can_glitch_red, 0xAAFF003F.toInt(), 2.2f, 90_000L, "ANGRY"),
        EnergyDrinkItem("can_ultra_white", "Ultra White", "Ледяной цитрус", R.drawable.ic_can_ultra_white, 0xAAFFFFFF.toInt(), 1.4f, 45_000L, "FOCUSED"),
        EnergyDrinkItem("can_banana_nitro", "Banana Nitro", "Банановый нитро-заряд", R.drawable.ic_can_banana_nitro, 0xAAFFEB3B.toInt(), 2.5f, 75_000L, "EXCITED"),
        EnergyDrinkItem("can_frost_blue", "Frost Blue", "Ледяная свежесть", R.drawable.ic_can_frost_blue, 0xAA00E5FF.toInt(), 1.6f, 55_000L, "CURIOUS"),
        EnergyDrinkItem("can_violet_punch", "Violet Punch", "Фиолетовый удар", R.drawable.ic_can_violet_punch, 0xAAE040FB.toInt(), 2.0f, 70_000L, "LOVE")
    )

    val CUSTOM_ITEMS = listOf(
        CustomItem("plush_shark", "Cyber Shark", "Плюшевая акула для объятий", R.drawable.ic_plush_shark, ItemCategory.PLUSH, "Обнимашки с акулой~"),
        CustomItem("plush_heart", "Heart Plush", "Мягкое сердечко", R.drawable.ic_item_heart_plush, ItemCategory.PLUSH, "Вот тебе сердечко!"),
        CustomItem("boba_tea", "Boba Tea", "Тапиока с трубочкой", R.drawable.ic_item_boba_tea, ItemCategory.FOOD, "Бульк-бульк... вкусно!"),
        CustomItem("hot_coffee", "Hot Coffee", "Горячий кофе", R.drawable.ic_item_hot_coffee, ItemCategory.DRINK, "Ммм, согревает..."),
        CustomItem("banana_snack", "Banana Snack", "Банановый перекус", R.drawable.ic_item_banana_snack, ItemCategory.FOOD, "Ням-ням, бананчик!"),
        CustomItem("energy_bar", "Energy Bar", "Энергетический батончик", R.drawable.ic_item_energy_bar, ItemCategory.FOOD, "Заряд получен!"),
        CustomItem("gamepad", "Arcade Gamepad", "Кибер-геймпад", R.drawable.ic_item_gamepad, ItemCategory.GADGET, "Играем? Я за!"),
        CustomItem("controller_pro", "Pro Controller", "Про-контроллер", R.drawable.ic_item_controller_pro, ItemCategory.GADGET, "Геймер-мод ON"),
        CustomItem("headphones", "Neon Headphones", "Неоновые наушники", R.drawable.ic_item_headphones, ItemCategory.GADGET, "Музыка в ушах~"),
        CustomItem("phone", "Mini Phone", "Мини-телефон", R.drawable.ic_item_phone, ItemCategory.GADGET, "Алло? Это Луня!"),
        CustomItem("star_sticker", "Star Sticker", "Звёздочка-наклейка", R.drawable.ic_item_star_sticker, ItemCategory.STICKER, "Ты звезда!"),
        CustomItem("flower_clip", "White Flower", "Белый цветок", R.drawable.ic_flower_white_clip, ItemCategory.STICKER, "Цветочек за ушком~"),
        CustomItem("react_hearts", "Heart Eyes", "Реакция: сердечки", R.drawable.ic_reaction_hearts, ItemCategory.REACTION, "Ты такой милый..."),
        CustomItem("react_angry", "Tsundere Face", "Реакция: цундере", R.drawable.ic_reaction_angry, ItemCategory.REACTION, "Н-не то чтобы я злилась!"),
        CustomItem("react_sleepy", "Sleepy Zzz", "Реакция: сонливость", R.drawable.ic_reaction_sleepy, ItemCategory.REACTION, "Ззз... ещё пять минуточек...")
    )

    val ITEMS get() = DRINKS // backward compat
}

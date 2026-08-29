package com.lunya.assistant.wardrobe

import com.lunya.assistant.R

data class OutfitSet(
    val setId: String,
    val setName: String,
    val description: String,
    val topRes: Int,
    val hornsRes: Int,
    val hairRes: Int,
    val glassesRes: Int,
    val defaultItemRes: Int,
    val auraColor: Int
)

enum class EquipmentSlot { HAND_ITEM, HEAD, BODY }

data class WardrobeItem(
    val id: String,
    val slot: EquipmentSlot,
    val name: String,
    val iconRes: Int
)

object MegaWardrobeCatalog {

    val OUTFIT_SETS = listOf(
        OutfitSet(
            setId = "set_nana_banana",
            setName = "Nana Banana Cozy Edition",
            description = "Пастельно-банановый оверсайз свитер, цветок за ушком и банка Banana Nitro",
            topRes = R.drawable.ic_outfit_nana_banana,
            hornsRes = R.drawable.ic_lunya_horns,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultItemRes = R.drawable.ic_can_banana_nitro,
            auraColor = 0x88FFEB3B.toInt()
        ),
        OutfitSet(
            setId = "set_toxic_street",
            setName = "Toxic Street Hacker",
            description = "Фирменное черное худи с неоново-зелеными потеками и кибер-рожками",
            topRes = R.drawable.ic_outfit_toxic_hoodie,
            hornsRes = R.drawable.ic_lunya_horns,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultItemRes = R.drawable.ic_can_toxic_lime,
            auraColor = 0x8839FF14.toInt()
        ),
        OutfitSet(
            setId = "set_cozy_femboy",
            setName = "Cozy Pastel Femboy",
            description = "Уютный розовый свитер крупной вязки и плюшевая акула",
            topRes = R.drawable.ic_outfit_cozy_sweater,
            hornsRes = R.drawable.ic_lunya_horns,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultItemRes = R.drawable.ic_plush_shark,
            auraColor = 0x66FFAAE6.toInt()
        ),
        OutfitSet(
            setId = "set_root_gothic",
            setName = "Crimson Root Cyber-Gothic",
            description = "Черная мантия с алым капюшоном и багровым разгоном",
            topRes = R.drawable.ic_outfit_glitch_cloak,
            hornsRes = R.drawable.ic_lunya_horns,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultItemRes = R.drawable.ic_can_glitch_red,
            auraColor = 0x99FF003F.toInt()
        ),
        OutfitSet(
            setId = "set_arcade_gamer",
            setName = "Arcade Neon Gamer",
            description = "Бомбер с циановыми полосами и кибер-геймпад",
            topRes = R.drawable.ic_outfit_gamer_bomber,
            hornsRes = R.drawable.ic_lunya_horns,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultItemRes = R.drawable.ic_item_gamepad,
            auraColor = 0x8800E5FF.toInt()
        ),
        OutfitSet(
            setId = "set_cyber_maid",
            setName = "Cyber Server / Tech Apron",
            description = "Кибернетический фартук и стаканчик Boba Tea с трубочкой",
            topRes = R.drawable.ic_outfit_cyber_maid,
            hornsRes = R.drawable.ic_lunya_horns,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultItemRes = R.drawable.ic_item_boba_tea,
            auraColor = 0x77E040FB.toInt()
        ),
        OutfitSet(
            setId = "set_midnight_sleep",
            setName = "Midnight Lo-Fi Cozy",
            description = "Ночная сорочка с золотыми звездами и горячий кофе",
            topRes = R.drawable.ic_outfit_midnight_sleep,
            hornsRes = R.drawable.ic_lunya_horns,
            hairRes = R.drawable.ic_lunya_hair,
            glassesRes = R.drawable.ic_lunya_glasses_round,
            defaultItemRes = R.drawable.ic_item_hot_coffee,
            auraColor = 0x66FFE680.toInt()
        )
    )

    val ALL_HAND_ITEMS = listOf(
        WardrobeItem("item_can_banana", EquipmentSlot.HAND_ITEM, "Nana Banana Nitro Can", R.drawable.ic_can_banana_nitro),
        WardrobeItem("item_snack_banana", EquipmentSlot.HAND_ITEM, "Nana Banana Dessert", R.drawable.ic_item_banana_snack),
        WardrobeItem("item_can_lime", EquipmentSlot.HAND_ITEM, "Toxic Lime Can", R.drawable.ic_can_toxic_lime),
        WardrobeItem("item_can_red", EquipmentSlot.HAND_ITEM, "Glitch Red Can", R.drawable.ic_can_glitch_red),
        WardrobeItem("item_can_white", EquipmentSlot.HAND_ITEM, "Ultra White Can", R.drawable.ic_can_ultra_white),
        WardrobeItem("item_can_violet", EquipmentSlot.HAND_ITEM, "Violet Punch Can", R.drawable.ic_can_violet_punch),
        WardrobeItem("item_can_frost", EquipmentSlot.HAND_ITEM, "Frost Blue Can", R.drawable.ic_can_frost_blue),
        WardrobeItem("item_shark", EquipmentSlot.HAND_ITEM, "Cyber Shark Plush", R.drawable.ic_plush_shark),
        WardrobeItem("item_boba", EquipmentSlot.HAND_ITEM, "Boba Tea Cup", R.drawable.ic_item_boba_tea),
        WardrobeItem("item_gamepad", EquipmentSlot.HAND_ITEM, "Arcade Gamepad", R.drawable.ic_item_gamepad),
        WardrobeItem("item_coffee", EquipmentSlot.HAND_ITEM, "Hot Coffee Mug", R.drawable.ic_item_hot_coffee)
    )
}

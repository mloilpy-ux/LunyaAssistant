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

    /** FIRST set = your reference character — default everywhere */
    val OUTFIT_SETS = listOf(
        OutfitSet(
            setId = "set_cozy_reference",
            setName = "Cozy Pastel Reference",
            description = "Фиолетовая шерсть, салатовые волосы, оленьи рожки, белый цветок, розовый оверсайз, круглые очки",
            topRes = R.drawable.ic_outfit_pastel_sweater,
            hornsRes = R.drawable.ic_lunya_antlers,
            hairRes = R.drawable.ic_lunya_hair_lime,
            glassesRes = R.drawable.ic_lunya_glasses_round_ref,
            defaultItemRes = R.drawable.ic_flower_white_clip,
            auraColor = 0x88E8B4D4.toInt()
        ),
        OutfitSet(
            setId = "set_nana_banana",
            setName = "Nana Banana Cozy Edition",
            description = "Банановый свитер + Banana Nitro",
            topRes = R.drawable.ic_outfit_nana_banana,
            hornsRes = R.drawable.ic_lunya_antlers,
            hairRes = R.drawable.ic_lunya_hair_lime,
            glassesRes = R.drawable.ic_lunya_glasses_round_ref,
            defaultItemRes = R.drawable.ic_can_banana_nitro,
            auraColor = 0x88FFEB3B.toInt()
        ),
        OutfitSet(
            setId = "set_toxic_street",
            setName = "Toxic Street Hacker",
            description = "Неоновое худи",
            topRes = R.drawable.ic_outfit_toxic_hoodie,
            hornsRes = R.drawable.ic_lunya_antlers,
            hairRes = R.drawable.ic_lunya_hair_lime,
            glassesRes = R.drawable.ic_lunya_glasses_round_ref,
            defaultItemRes = R.drawable.ic_can_toxic_lime,
            auraColor = 0x8839FF14.toInt()
        ),
        OutfitSet(
            setId = "set_cyber_maid",
            setName = "Cyber Maid",
            description = "Кибер-фартук + Boba",
            topRes = R.drawable.ic_outfit_cyber_maid,
            hornsRes = R.drawable.ic_lunya_antlers,
            hairRes = R.drawable.ic_lunya_hair_lime,
            glassesRes = R.drawable.ic_lunya_glasses_round_ref,
            defaultItemRes = R.drawable.ic_item_boba_tea,
            auraColor = 0x77E040FB.toInt()
        ),
        OutfitSet(
            setId = "set_arcade_gamer",
            setName = "Arcade Neon Gamer",
            description = "Бомбер + геймпад",
            topRes = R.drawable.ic_outfit_gamer_bomber,
            hornsRes = R.drawable.ic_lunya_antlers,
            hairRes = R.drawable.ic_lunya_hair_lime,
            glassesRes = R.drawable.ic_lunya_glasses_round_ref,
            defaultItemRes = R.drawable.ic_item_gamepad,
            auraColor = 0x8800E5FF.toInt()
        ),
        OutfitSet(
            setId = "set_midnight_sleep",
            setName = "Midnight Lo-Fi",
            description = "Ночная сорочка + кофе",
            topRes = R.drawable.ic_outfit_midnight_sleep,
            hornsRes = R.drawable.ic_lunya_antlers,
            hairRes = R.drawable.ic_lunya_hair_lime,
            glassesRes = R.drawable.ic_lunya_glasses_round_ref,
            defaultItemRes = R.drawable.ic_item_hot_coffee,
            auraColor = 0x66FFE680.toInt()
        ),
        OutfitSet(
            setId = "set_root_gothic",
            setName = "Crimson Root",
            description = "Глитч-мантия",
            topRes = R.drawable.ic_outfit_glitch_cloak,
            hornsRes = R.drawable.ic_lunya_antlers,
            hairRes = R.drawable.ic_lunya_hair_lime,
            glassesRes = R.drawable.ic_lunya_glasses_round_ref,
            defaultItemRes = R.drawable.ic_can_glitch_red,
            auraColor = 0x99FF003F.toInt()
        )
    )

    val ALL_HAND_ITEMS = listOf(
        WardrobeItem("item_flower", EquipmentSlot.HAND_ITEM, "White Flower", R.drawable.ic_flower_white_clip),
        WardrobeItem("item_can_banana", EquipmentSlot.HAND_ITEM, "Banana Nitro", R.drawable.ic_can_banana_nitro),
        WardrobeItem("item_shark", EquipmentSlot.HAND_ITEM, "Cyber Shark", R.drawable.ic_plush_shark),
        WardrobeItem("item_boba", EquipmentSlot.HAND_ITEM, "Boba Tea", R.drawable.ic_item_boba_tea),
        WardrobeItem("item_gamepad", EquipmentSlot.HAND_ITEM, "Gamepad", R.drawable.ic_item_gamepad),
        WardrobeItem("item_coffee", EquipmentSlot.HAND_ITEM, "Hot Coffee", R.drawable.ic_item_hot_coffee),
        WardrobeItem("item_heart", EquipmentSlot.HAND_ITEM, "Heart Plush", R.drawable.ic_item_heart_plush),
        WardrobeItem("item_headphones", EquipmentSlot.HAND_ITEM, "Headphones", R.drawable.ic_item_headphones)
    )
}

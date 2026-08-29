package com.lunya.assistant.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.lunya.assistant.R
import com.lunya.assistant.wardrobe.MegaWardrobeCatalog

/**
 * Modular avatar that layers body + hair + horns + glasses + outfit + hand item.
 */
class ModularAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val body = ImageView(context)
    private val outfit = ImageView(context)
    private val hair = ImageView(context)
    private val horns = ImageView(context)
    private val glasses = ImageView(context)
    private val handItem = ImageView(context)

    init {
        listOf(body, outfit, hair, horns, glasses, handItem).forEach {
            it.scaleType = ImageView.ScaleType.FIT_CENTER
            addView(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
        body.setImageResource(R.drawable.ic_lunya_base_body)
        applyOutfit(MegaWardrobeCatalog.OUTFIT_SETS.first())
    }

    fun applyOutfit(set: com.lunya.assistant.wardrobe.OutfitSet) {
        outfit.setImageResource(set.topRes)
        hair.setImageResource(set.hairRes)
        horns.setImageResource(set.hornsRes)
        glasses.setImageResource(set.glassesRes)
        handItem.setImageResource(set.defaultItemRes)
    }

    fun setHandItem(resId: Int) {
        handItem.setImageResource(resId)
    }
}

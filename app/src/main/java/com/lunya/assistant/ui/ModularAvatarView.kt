package com.lunya.assistant.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.lunya.assistant.R
import com.lunya.assistant.animation.BodyPart
import com.lunya.assistant.animation.LunyaAnimationDirector
import com.lunya.assistant.animation.PartTransform
import com.lunya.assistant.wardrobe.MegaWardrobeCatalog
import com.lunya.assistant.wardrobe.OutfitSet

/**
 * Layered avatar — DEFAULT is your reference character:
 * purple body, lime hair, antlers, pink sweater, round glasses, white flower.
 */
class ModularAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val body = ImageView(context)
    private val ears = ImageView(context)
    private val outfit = ImageView(context)
    private val hair = ImageView(context)
    private val antlers = ImageView(context)
    private val glasses = ImageView(context)
    private val flower = ImageView(context)
    private val handItem = ImageView(context)

    val director = LunyaAnimationDirector()

    private val partViews = mapOf(
        BodyPart.TORSO to body,
        BodyPart.EARS_LEFT to ears,
        BodyPart.OUTFIT to outfit,
        BodyPart.HAIR to hair,
        BodyPart.ANTLERS to antlers,
        BodyPart.ACCESSORY_HEAD to glasses,
        BodyPart.ACCESSORY_HAND to handItem
    )

    private val frameRunnable = object : Runnable {
        override fun run() {
            applyPose(director.evaluate())
            postOnAnimation(this)
        }
    }

    init {
        // z-order: body -> ears -> outfit -> hair -> antlers -> glasses -> flower -> hand
        listOf(body, ears, outfit, hair, antlers, glasses, flower, handItem).forEach {
            it.scaleType = ImageView.ScaleType.FIT_CENTER
            addView(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        // === YOUR CHARACTER (reference art) as default ===
        body.setImageResource(R.drawable.ic_lunya_base_body_purple)
        ears.setImageResource(R.drawable.ic_lunya_ears_purple)
        hair.setImageResource(R.drawable.ic_lunya_hair_lime)
        antlers.setImageResource(R.drawable.ic_lunya_antlers)
        glasses.setImageResource(R.drawable.ic_lunya_glasses_round_ref)
        outfit.setImageResource(R.drawable.ic_outfit_pastel_sweater)
        flower.setImageResource(R.drawable.ic_flower_white_clip)
        flower.visibility = VISIBLE
        handItem.setImageResource(R.drawable.ic_flower_white_clip)

        // Also register as first wardrobe set if present
        MegaWardrobeCatalog.OUTFIT_SETS.firstOrNull()?.let { applyOutfit(it) }

        postOnAnimation(frameRunnable)
    }

    fun applyOutfit(set: OutfitSet) {
        outfit.setImageResource(set.topRes)
        hair.setImageResource(set.hairRes)
        antlers.setImageResource(set.hornsRes)
        glasses.setImageResource(set.glassesRes)
        handItem.setImageResource(set.defaultItemRes)

        // Always keep purple body + purple ears for this character
        body.setImageResource(R.drawable.ic_lunya_base_body_purple)
        ears.setImageResource(R.drawable.ic_lunya_ears_purple)

        if (set.setId == "set_cozy_reference" || set.hairRes == R.drawable.ic_lunya_hair_lime) {
            flower.setImageResource(R.drawable.ic_flower_white_clip)
            flower.visibility = VISIBLE
        } else {
            flower.visibility = GONE
        }
    }

    fun setHandItem(resId: Int) {
        handItem.setImageResource(resId)
    }

    /** Reset to pure reference look */
    fun resetToReferenceCharacter() {
        body.setImageResource(R.drawable.ic_lunya_base_body_purple)
        ears.setImageResource(R.drawable.ic_lunya_ears_purple)
        hair.setImageResource(R.drawable.ic_lunya_hair_lime)
        antlers.setImageResource(R.drawable.ic_lunya_antlers)
        glasses.setImageResource(R.drawable.ic_lunya_glasses_round_ref)
        outfit.setImageResource(R.drawable.ic_outfit_pastel_sweater)
        flower.setImageResource(R.drawable.ic_flower_white_clip)
        flower.visibility = VISIBLE
        handItem.setImageResource(R.drawable.ic_flower_white_clip)
    }

    private fun applyPose(pose: Map<BodyPart, PartTransform>) {
        partViews.forEach { (part, view) ->
            val tf = pose[part] ?: return@forEach
            view.translationX = tf.offsetX
            view.translationY = tf.offsetY
            view.rotation = tf.rotation
            view.scaleX = tf.scaleX * (1f + tf.squash * 0.3f)
            view.scaleY = tf.scaleY * (1f - tf.squash * 0.3f)
            view.alpha = tf.alpha
        }
        // Head-linked layers follow head transform
        pose[BodyPart.HEAD]?.let { head ->
            listOf(hair, antlers, glasses, flower, ears).forEach { v ->
                v.translationX += head.offsetX * 0.9f
                v.translationY += head.offsetY * 0.9f
                v.rotation += head.rotation * 0.85f
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(frameRunnable)
    }
}

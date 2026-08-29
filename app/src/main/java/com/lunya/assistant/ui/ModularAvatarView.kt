package com.lunya.assistant.ui

import android.content.Context
import android.graphics.Canvas
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
 * Modular avatar with full per-body-part procedural animation.
 * Every part (head, ears, hair, antlers, arms, eyes...) moves independently.
 */
class ModularAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val body = ImageView(context)
    private val outfit = ImageView(context)
    private val hair = ImageView(context)
    private val antlers = ImageView(context)
    private val glasses = ImageView(context)
    private val handItem = ImageView(context)
    private val flower = ImageView(context)

    val director = LunyaAnimationDirector()

    private val partViews = mapOf(
        BodyPart.TORSO to body,
        BodyPart.OUTFIT to outfit,
        BodyPart.HAIR to hair,
        BodyPart.ANTLERS to antlers,
        BodyPart.ACCESSORY_HEAD to glasses,
        BodyPart.ACCESSORY_HAND to handItem,
        BodyPart.HEAD to flower // flower rides on head
    )

    private val frameRunnable = object : Runnable {
        override fun run() {
            applyPose(director.evaluate())
            postOnAnimation(this)
        }
    }

    init {
        listOf(body, outfit, hair, antlers, glasses, flower, handItem).forEach {
            it.scaleType = ImageView.ScaleType.FIT_CENTER
            addView(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
        body.setImageResource(R.drawable.ic_lunya_base_body_purple)
        applyOutfit(MegaWardrobeCatalog.OUTFIT_SETS.first())
        postOnAnimation(frameRunnable)
    }

    fun applyOutfit(set: OutfitSet) {
        outfit.setImageResource(set.topRes)
        hair.setImageResource(set.hairRes)
        antlers.setImageResource(set.hornsRes)
        glasses.setImageResource(set.glassesRes)
        handItem.setImageResource(set.defaultItemRes)
        // flower clip for reference set
        if (set.setId == "set_cozy_reference") {
            flower.setImageResource(R.drawable.ic_flower_white_clip)
            flower.visibility = VISIBLE
        } else {
            flower.visibility = GONE
        }
    }

    fun setHandItem(resId: Int) {
        handItem.setImageResource(resId)
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
        // head-linked parts also get head transform
        pose[BodyPart.HEAD]?.let { head ->
            listOf(hair, antlers, glasses, flower).forEach { v ->
                v.translationX += head.offsetX * 0.9f
                v.translationY += head.offsetY * 0.9f
                v.rotation += head.rotation * 0.8f
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(frameRunnable)
    }
}

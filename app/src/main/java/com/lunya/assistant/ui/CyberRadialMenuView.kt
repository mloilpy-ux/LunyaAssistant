package com.lunya.assistant.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * Cyber-style radial quick-action menu around Lunya.
 */
class CyberRadialMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF39FF14.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    var itemCount: Int = 6

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = minOf(cx, cy) * 0.7f

        for (i in 0 until itemCount) {
            val angle = Math.toRadians((360.0 / itemCount) * i - 90)
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()
            canvas.drawCircle(x, y, 18f, paint)
        }
    }
}

package com.lunya.assistant.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.lunya.assistant.MainActivity
import com.lunya.assistant.R
import com.lunya.assistant.animation.Emotion
import com.lunya.assistant.core.LunyaBrain
import com.lunya.assistant.generator.InfiniteInteractionMatrix
import com.lunya.assistant.interaction.MillionInteractionHub
import com.lunya.assistant.system.AppAwarenessEngine
import com.lunya.assistant.ui.ModularAvatarView
import com.lunya.assistant.wardrobe.MegaWardrobeCatalog

/**
 * Floating Lunya overlay with full interaction hub, app awareness, emotions & motions.
 */
class LunyaOverlayService : Service() {

    companion object {
        const val CHANNEL_ID = "lunya_overlay_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_APP_CHANGED = "com.lunya.ACTION_APP_CHANGED"
    }

    private lateinit var windowManager: WindowManager
    private lateinit var container: FrameLayout
    private lateinit var avatarView: ModularAvatarView
    private lateinit var speechBubble: TextView
    private lateinit var layoutParams: WindowManager.LayoutParams

    private val brain = LunyaBrain()
    private lateinit var hub: MillionInteractionHub
    private lateinit var appAwareness: AppAwarenessEngine

    private var lastX = 0
    private var lastY = 0
    private var startX = 0f
    private var startY = 0f

    private val appReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != ACTION_APP_CHANGED) return
            val pkg = intent.getStringExtra("package_name") ?: return
            val label = intent.getStringExtra("app_label")
            appAwareness.onForegroundApp(pkg, label)
            val fb = hub.handle(InfiniteInteractionMatrix.UserAction.OPEN_APP)
            showSpeech(fb.spoken)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        avatarView = ModularAvatarView(this)
        hub = MillionInteractionHub(avatarView.director)
        appAwareness = AppAwarenessEngine(this, avatarView.director).also {
            it.onAppReaction = { profile ->
                avatarView.director.currentEmotion = profile.emotion
                showSpeech(profile.phrase)
            }
        }

        // Apply first wardrobe set (cozy reference if available)
        MegaWardrobeCatalog.OUTFIT_SETS.firstOrNull()?.let { avatarView.applyOutfit(it) }

        speechBubble = TextView(this).apply {
            setBackgroundResource(R.drawable.bg_speech_bubble)
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 13f
            setPadding(28, 16, 28, 16)
            visibility = TextView.GONE
            maxWidth = 420
        }

        container = FrameLayout(this).apply {
            addView(avatarView, FrameLayout.LayoutParams(280, 360))
            addView(speechBubble, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).also {
                it.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                it.topMargin = 8
            })
        }

        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 80
            y = 400
        }

        container.setOnTouchListener { _, event -> handleTouch(event) }
        windowManager.addView(container, layoutParams)

        val filter = IntentFilter(ACTION_APP_CHANGED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(appReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(appReceiver, filter)
        }

        // Morning line on start
        val morning = hub.morningLine()
        showSpeech(morning.spoken)
    }

    private fun handleTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.rawX
                startY = event.rawY
                lastX = layoutParams.x
                lastY = layoutParams.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                layoutParams.x = lastX + (event.rawX - startX).toInt()
                layoutParams.y = lastY + (event.rawY - startY).toInt()
                windowManager.updateViewLayout(container, layoutParams)
                return true
            }
            MotionEvent.ACTION_UP -> {
                val dx = event.rawX - startX
                val dy = event.rawY - startY
                val dist = dx * dx + dy * dy
                val fb = when {
                    dist < 25 * 25 && event.eventTime - event.downTime > 500 -> {
                        brain.onUserInteraction("overclock")
                        hub.handle(InfiniteInteractionMatrix.UserAction.LONG_PRESS)
                    }
                    dist < 25 * 25 -> {
                        brain.onUserInteraction("tap")
                        hub.handle(InfiniteInteractionMatrix.UserAction.TAP)
                    }
                    else -> {
                        brain.onUserInteraction("pet")
                        hub.handle(InfiniteInteractionMatrix.UserAction.PET)
                    }
                }
                showSpeech(fb.spoken)
                // show thought briefly in log / optional second bubble
                return true
            }
        }
        return false
    }

    private fun showSpeech(text: String) {
        speechBubble.text = text
        speechBubble.visibility = TextView.VISIBLE
        speechBubble.postDelayed({
            speechBubble.visibility = TextView.GONE
        }, 3200)
    }

    fun onNotificationEvent() {
        val fb = hub.handle(InfiniteInteractionMatrix.UserAction.NOTIFICATION)
        showSpeech(fb.spoken)
    }

    fun giveRandomItem() {
        val fb = hub.giveProceduralItem()
        showSpeech(fb.spoken)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Lunya Overlay",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = getString(R.string.service_notification_desc) }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val pi = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_desc))
            .setSmallIcon(R.drawable.ic_lunya_base_body)
            .setContentIntent(pi)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(appReceiver) } catch (_: Exception) {}
        try { windowManager.removeView(container) } catch (_: Exception) {}
        brain.shutdown()
    }
}

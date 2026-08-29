package com.lunya.assistant.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.lunya.assistant.service.LunyaOverlayService.Companion.ACTION_APP_CHANGED

/**
 * Feeds foreground app changes to Overlay / AppAwarenessEngine
 * so Lunya can react to YouTube, games, chats, etc.
 */
class LunyaAccessibilityService : AccessibilityService() {

    private var lastPackage: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) return

        val pkg = event.packageName?.toString() ?: return
        if (pkg == lastPackage) return
        if (pkg == packageName) return // ignore self
        lastPackage = pkg

        val label = try {
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(pkg, 0)
            ).toString()
        } catch (_: Exception) {
            pkg
        }

        // Broadcast to overlay service
        sendBroadcast(Intent(ACTION_APP_CHANGED).apply {
            setPackage(packageName)
            putExtra("package_name", pkg)
            putExtra("app_label", label)
        })
    }

    override fun onInterrupt() {}
}

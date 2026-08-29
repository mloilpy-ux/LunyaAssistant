package com.lunya.assistant.ai

import android.service.notification.StatusBarNotification
import android.util.Log

/**
 * AI that classifies and prioritizes incoming notifications for Lunya.
 */
class SmartNotificationAI {

    fun analyze(sbn: StatusBarNotification): NotificationPriority {
        val title = sbn.notification.extras.getString("android.title") ?: ""
        val text = sbn.notification.extras.getString("android.text") ?: ""
        val pkg = sbn.packageName

        return when {
            pkg.contains("messaging") || pkg.contains("telegram") || pkg.contains("whatsapp") ->
                NotificationPriority.HIGH
            title.contains("urgent", ignoreCase = true) || text.contains("!") ->
                NotificationPriority.HIGH
            else -> NotificationPriority.NORMAL
        }.also {
            Log.d("SmartNotifAI", "Analyzed $pkg -> $it")
        }
    }

    enum class NotificationPriority { LOW, NORMAL, HIGH, CRITICAL }
}

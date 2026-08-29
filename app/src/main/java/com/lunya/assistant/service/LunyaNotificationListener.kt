package com.lunya.assistant.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.lunya.assistant.service.LunyaOverlayService.Companion.ACTION_APP_CHANGED

/**
 * Notifies Lunya about new notifications -> SURPRISED / ear perk + phrase.
 */
class LunyaNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null || sbn.packageName == packageName) return
        // Reuse app-changed channel with a notification flag via package
        sendBroadcast(Intent(ACTION_APP_CHANGED).apply {
            setPackage(packageName)
            putExtra("package_name", sbn.packageName)
            putExtra("app_label", "notification:${sbn.packageName}")
        })
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {}
}

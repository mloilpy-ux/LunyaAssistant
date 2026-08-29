package com.lunya.assistant.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.lunya.assistant.service.LunyaOverlayService.Companion.ACTION_APP_CHANGED

/** Sends notification events to the live Lunya overlay. */
class LunyaNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null || sbn.packageName == packageName) return
        sendBroadcast(Intent(ACTION_APP_CHANGED).apply {
            setPackage(packageName)
            putExtra("package_name", sbn.packageName)
            putExtra("app_label", "notification:${sbn.packageName}")
            putExtra("event", "notification")
        })
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) = Unit
}

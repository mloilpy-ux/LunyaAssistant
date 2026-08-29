package com.lunya.assistant.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class LunyaNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {}
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {}
}

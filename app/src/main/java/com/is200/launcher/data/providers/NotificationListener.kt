package com.is200.launcher.data.providers

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListener :
    NotificationListenerService() {

    override fun onNotificationPosted(
        sbn: StatusBarNotification?
    ) {

        super.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification?
    ) {

        super.onNotificationRemoved(sbn)
    }
}
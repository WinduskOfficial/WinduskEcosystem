package com.windusk.testclientapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.windusk.clientBasics.service.EcosystemClient
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import com.windusk.testclientapp.TestSharingChoiserActivity.Companion.startChoiserActivity

class TestEcosystemClient: EcosystemClient() {
    override val foregroundNotification by lazy {
        foregroundAlarm(this)
    }

    override fun onConflict(subscribition: PrioritySubscribition) {
        startChoiserActivity(subscribition)
    }

    fun foregroundAlarm(context: Context): Notification {
        val id = "Foreground"
        val name = "Уведомление о фоновой работе"
        val descriptionText = "Системный канал"

        val importance = NotificationManager.IMPORTANCE_NONE
        val channel = NotificationChannel(id, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return Notification.Builder(context, id)
            .setContentTitle("Кентавр Про")
            .setContentText("Работает в фоновом режиме")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }
}

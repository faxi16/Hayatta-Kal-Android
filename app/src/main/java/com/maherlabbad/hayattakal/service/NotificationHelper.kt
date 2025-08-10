package com.maherlabbad.hayattakal.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {
    const val CHANNEL_ID_SERVICE = "quake_service_channel"
    const val CHANNEL_ID_ALERT = "quake_alert_channel"
    const val SERVICE_NOTIF_ID = 1001

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(NotificationManager::class.java)
            nm?.let {
                val serviceChannel = NotificationChannel(
                    CHANNEL_ID_SERVICE,
                    "Deprem Takip Servisi",
                    NotificationManager.IMPORTANCE_LOW
                ).apply { description = "Arka planda deprem takibi çalışıyor" }

                val alertChannel = NotificationChannel(
                    CHANNEL_ID_ALERT,
                    "Deprem Uyarıları",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Önemli deprem uyarıları" }

                it.createNotificationChannel(serviceChannel)
                it.createNotificationChannel(alertChannel)
            }
        }
    }

    fun createServiceNotification(context: Context, text: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID_SERVICE)
            .setContentTitle("Deprem Takibi Aktif")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
    }

    fun createAlertNotification(context: Context, title: String, text: String) =
        NotificationCompat.Builder(context, CHANNEL_ID_ALERT)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
}

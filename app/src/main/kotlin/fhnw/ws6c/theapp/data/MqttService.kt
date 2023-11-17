package fhnw.ws6c.theapp.data
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent


import android.app.Service
import android.os.IBinder
import androidx.core.app.NotificationCompat
import fhnw.ws6c.R


class MqttService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // Create a notification channel
        createNotificationChannel()

        // Build a notification for the foreground service
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MQTT Service")
            .setContentText("MQTT is running in the background")
            .setSmallIcon(R.drawable.aloe_happy)
            .build()

        // Start the service in the foreground
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Disconnect MQTT when the service is destroyed
        MqttConnector.disconnect()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "MQTT Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "mqtt_channel"
        private const val NOTIFICATION_ID = 1
    }
}
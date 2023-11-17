package fhnw.ws6c.theapp.data

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class PlantNotificationService (private val context: Context) {
    fun showBasicNotification() {
        val notification = NotificationCompat.Builder(context, "water_notification")
            //.setSmallIcon(R.drawable.water_icon)
            .setContentTitle("AAAAA!!")
            .setContentText("A plant needs some water")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Dissapears after clicking the notification
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(Random.nextInt(), notification)
    }
}
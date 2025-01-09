package fhnw.ws6c.theapp.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import fhnw.ws6c.MainActivity
import fhnw.ws6c.R

class NotificationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "fhnw.ws6c.theapp.notifications"
    lateinit var pendingIntent: PendingIntent
    private val GROUP_KEY_AAAA = "group_key_aaaa"
    private val GROUP_KEY_OK = "group_key_ok"

    fun showAAAANotification(plantName: String) {
        val notificationId = System.currentTimeMillis().toInt()

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("AAAA!!")
            .setContentText("$plantName braucht Wasser!")
            .setSmallIcon(R.drawable.aloe_sad)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_AAAA)
            .setAutoCancel(true)
            .build()

        val summaryNotification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("AAAA!!")
            .setContentText("Mehrere Pflanzen benötigen Wasser!")
            .setSmallIcon(R.drawable.aloe_sad)
            .setStyle(NotificationCompat.InboxStyle()
                .setSummaryText("Giessen"))
            .setGroup(GROUP_KEY_AAAA)
            .setGroupSummary(true)
            .build()

        notificationManager.notify(notificationId, notification)

        notificationManager.notify(0, summaryNotification)
    }
    fun showOKNotification(plantName: String) {
        val notificationId = System.currentTimeMillis().toInt()

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Slurp")
            .setContentText("$plantName hat wieder genügend Wasser.")
            .setSmallIcon(R.drawable.aloe_happy)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_OK)
            .setAutoCancel(true)
            .build()

        val summaryNotification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Slurp")
            .setContentText("Mehrere Pflanzen haben genügend Wasser!")
            .setSmallIcon(R.drawable.aloe_sad)
            .setStyle(NotificationCompat.InboxStyle()
                .setSummaryText("Slurp"))
            .setGroup(GROUP_KEY_OK)
            .setGroupSummary(true)
            .build()

        notificationManager.notify(notificationId, notification)

        notificationManager.notify(1, summaryNotification)
    }

    fun setupNotification() {
        Log.e("NotificationService", "Helooooo")

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val sound = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.scream)

        Log.d("notification", "use this sound: $sound")

        // Create a notification channel (required for Android Oreo and above)
        val channel = NotificationChannel(
            channelId,
            "PlAAAAnt Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.setSound(
            Uri.parse("android.resource://" + context.packageName + "/" + R.raw.scream),
            audioAttributes
        )

        notificationManager.createNotificationChannel(channel)

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

}
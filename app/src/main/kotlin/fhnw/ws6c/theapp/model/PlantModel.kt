package fhnw.ws6c.theapp.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import fhnw.ws6c.MainActivity
import fhnw.ws6c.R
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.MqttService
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.data.defaultPlant
import fhnw.ws6c.theapp.receiver.MqttStaticReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class PlantModel(
    val context: Context,
    private val firebaseService: FirebaseService,
) {

    private val backgroundJob = SupervisorJob()
    private val modelScope = CoroutineScope(backgroundJob + Dispatchers.IO)

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "fhnw.ws6c.theapp.notifications"
    lateinit var pendingIntent: PendingIntent
    private val GROUP_KEY_AAAA = "group_key_aaaa"
    private val GROUP_KEY_OK = "group_key_ok"

    var isLoading by mutableStateOf(false)

    var currentScreen by mutableStateOf(Screen.LOGIN)
    var plantList by mutableStateOf<List<Plant>>(emptyList())
    var plantsThatNeedWaterList by mutableStateOf<List<Plant>>(emptyList())
    var currentPlant by mutableStateOf(if (plantList.isNotEmpty()) plantList[0] else defaultPlant())

    var notificationMessage by mutableStateOf("")  //TODO: wird noch nirgends angezeigt
    var connectionFailed by mutableStateOf(false)
        private set
    var firebaseError by mutableStateOf(false)
        private set

    val user = Firebase.auth.currentUser

    init {
        MqttStaticReceiver.setPlantModel(this)
        setupNotification()
    }

    fun connectAndSubscribe() {
        // Start the MQTT Service
        val intent = Intent(context, MqttService::class.java)
        context.startService(intent)
    }



    fun resetConnectionFailure() {
        connectionFailed = false
    }

    fun getPlants() {
        isLoading = true
        modelScope.launch {
            firebaseService.getPlants(
                onSuccess = {
                    plantList = it
                    getDbMeasurements()
                },
                onFailure = {
                    Log.d("notification", "Getting Plants failed. $it")
                    notificationMessage = it
                    firebaseError = true
                }
            )

        }
        isLoading = false
    }


    fun addMeasurementToPlant(measurement: Measurement) {
        //persist in firebase
        modelScope.launch {
            firebaseService.addMeasurementToPlant(
                measurement = measurement,
                onFailure = {
                    onFirebaseError(it)
                })
        }
        //add to plant in memory
        for (plant in plantList) {
            if (plant.sensorId == measurement.sensorId) {
                plant.measurements.apply { add(measurement) }
                checkIfWaterNeeded(plant, measurement, true)
                break
            }
        }
    }

    private fun checkIfWaterNeeded(plant: Plant, measurement: Measurement, notify: Boolean) {
        // notification if needsWater changes
        if ((plant.needsWater.value == false || plant.needsWater.value == null) && measurement.humidity < plant.minHumidity) {
            if (notify) showAAAANotification(plant.name)
            plant.needsWater.value = true
            plantsThatNeedWaterList += plant
        } else if ((plant.needsWater.value == true || plant.needsWater.value == null) && measurement.humidity > plant.minHumidity) {
            if (notify) showOKNotification(plant.name)
            plant.needsWater.value = false
            plantsThatNeedWaterList -= plant
        }
    }

    // get Measurements from Firebase and add to each known plant
    private fun getDbMeasurements() {
        modelScope.launch {
            firebaseService.getDbMeasurements(
                onSuccess = {
                    for (measurement in it) {
                        for (plant in plantList) {
                            if (plant.sensorId == measurement.sensorId) {
                                plant.measurements.apply { add(measurement) }
                                break
                            }
                        }
                    }
                    // check with last Measurement if water is needed
                    for (plant in plantList) {
                        if (plant.measurements.lastOrNull() != null) {
                            checkIfWaterNeeded(plant, plant.measurements.last(), false)
                        }
                    }
                },
                onFailure = {
                    onFirebaseError(it)
                }
            )
        }
    }

    fun counterPlantsThatNeedWater(): Int = plantList.count { it.needsWater.value == true }

    private fun setupNotification() {
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

    private fun showAAAANotification(plantName: String) {
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
    private fun showOKNotification(plantName: String) {
        val notificationId = System.currentTimeMillis().toInt()

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Slurp")
            .setContentText("$plantName hat wieder genügend Wasser")
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

    private fun onFirebaseError(message: String) {
        notificationMessage = message
        firebaseError = true
    }

}
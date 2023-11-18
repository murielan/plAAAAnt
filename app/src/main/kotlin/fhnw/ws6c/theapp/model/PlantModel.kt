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
import fhnw.ws6c.MainActivity
import fhnw.ws6c.R
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.MqttConnector
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.data.defaultPlant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class PlantModel(
    private val context: Context,
    private val mqttConnector: MqttConnector,
    private val firebaseService: FirebaseService,
) {

    private val backgroundJob = SupervisorJob()
    private val modelScope = CoroutineScope(backgroundJob + Dispatchers.IO)

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "fhnw.ws6c.theapp.notifications"
    lateinit var pendingIntent : PendingIntent

    var currentScreen by mutableStateOf(Screen.HOME)
    var plantList by mutableStateOf<List<Plant>>(emptyList())
    var currentPlant by mutableStateOf(if (plantList.isNotEmpty()) plantList[0] else defaultPlant())

    private var notificationMessage by mutableStateOf("")  //TODO: wird noch nirgends angezeigt

    init {
        setupNotification()
    }

    fun connectAndSubscribe() {
        mqttConnector.connectAndSubscribe(
            onNewMessage = {
                addMeasurementToPlant(Measurement(it))
            },
            onError = { _, p ->
                notificationMessage = p
            })
    }

    fun getPlants() {
        val job = modelScope.launch {
            firebaseService.getPlants {
                plantList = it
            }
        }
        //get FirebaseMeasurements after plants are loaded
        job.invokeOnCompletion { getDbMeasurements() }
    }


    private fun addMeasurementToPlant(measurement: Measurement) {
        //persist in firebase
        modelScope.launch {
            firebaseService.addMeasurementToPlant(measurement = measurement)
        }
        //add to plant in memory
        for (plant in plantList) {
            if (plant.sensorId == measurement.sensorId) {
                plant.measurements.apply { add(measurement) }
                checkIfWaterNeeded(plant, measurement)
                break
            }
        }
    }

    private fun checkIfWaterNeeded(plant: Plant, measurement: Measurement) {
        // notification if needsWater changes
        if (!plant.needsWater.value && measurement.humidity < plant.minHumidity) {
            showNotification(plant.name)
            plant.needsWater.value = true
        } else if (plant.needsWater.value && measurement.humidity > plant.minHumidity) {
            plant.needsWater.value = false
        }
    }

    // get Measurements from Firebase and add to each known plant
    private fun getDbMeasurements() {
        modelScope.launch {
            firebaseService.getDbMeasurements {
                for (measurement in it){
                    for (plant in plantList) {
                        if (plant.sensorId == measurement.sensorId) {
                            plant.measurements.apply { add(measurement) }
                            // checkIfWaterNeeded(plant, measurement) - if app is running and gets all measurements from mqtt, this is not needed here (it fails because it tries to handle over 1600 measurements)
                            break
                        }
                    }
                }
            }
        }
    }

    fun counterPlantsThatNeedWater(): Int = plantList.count { it.needsWater.value }

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

        channel.setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.scream), audioAttributes)

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

    private fun showNotification(plantName: String) {
        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("AAAA!!")
            .setContentText("$plantName braucht Wasser!")
            .setSmallIcon(R.drawable.aloe_sad)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(1, notification)
    }

}
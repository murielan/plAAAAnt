package fhnw.ws6c.theapp.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fhnw.ws6c.MainActivity
import fhnw.ws6c.R
import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.MqttConnector
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.data.PlantRepository


class PlantModel(private val context: Context, plantRepo: PlantRepository) {
    var title = "PlAAAAnt"
    var boolean by mutableStateOf(true)
    var currentScreen by mutableStateOf(Screen.HOME)

    var plantList: MutableList<Plant> = plantRepo.getPlants()
    var currentPlant by mutableStateOf(plantList[0])

    private val mqttBroker = "broker.hivemq.com"
    private val topic = "fhnw/ws6c/plaaaant"

    private val mqttConnector by lazy { MqttConnector(context, mqttBroker) }

    private var notificationMessage by mutableStateOf("")

    // firebase
    val db = Firebase.firestore

    fun connectAndSubscribe() {
        mqttConnector.connectAndSubscribe(topic = topic,
            onNewMessage = {
                addMeasurementToPlant(Measurement(it))
            },
            onError = { _, p ->
                notificationMessage = p
            })
        getDbMeasurements()
    }

    private fun addMeasurementToPlant(measurement: Measurement) {
        // Add a new document with a generated ID
        /*        db.collection("measurements")
                    .add(measurement)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding measurement", e)
                    }*/

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
        if(!plant.needsWater.value && measurement.humidity < plant.minHumidity){
            showNotification(plant.name)
            plant.needsWater.value = true
        } else if(plant.needsWater.value && measurement.humidity > plant.minHumidity) {
            plant.needsWater.value = false
        }
    }

    private fun getDbMeasurements() {
        db.collection("measurements")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun counterPlantsThatNeedWater(): Int {
        var count = 0
        for (plant in plantList) {
            if (plant.needsWater.value)
                count++
        }
        return count
    }
    private fun showNotification(plantName: String) {
        val channelId = "fhnw.ws6c.theapp.notifications"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (required for Android Oreo and above)
        val channel = NotificationChannel(
            channelId,
            "PlAAAAnt Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

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
package fhnw.ws6c.theapp.model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.MqttConnector
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.data.PlantRepository


class PlantModel(plantRepo: PlantRepository) {
    var title = "PlAAAAnt"
    var boolean by mutableStateOf(true)
    var currentScreen by mutableStateOf(Screen.HOME)

    //val dataStore: DataStore<Preferences> = context.createDataStore(name = "user_preferences")

    var plantList: MutableList<Plant> = plantRepo.getPlants()
    var currentPlant by mutableStateOf(plantList[0])

    private val mqttBroker = "broker.hivemq.com"
    private val topic = "fhnw/ws6c/plaaaant"

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }

    private var notificationMessage by mutableStateOf("")

    var measurementsReceived by mutableIntStateOf(0)

    val db = Firebase.firestore

    fun connectAndSubscribe() {
        mqttConnector.connectAndSubscribe(topic = topic,
            onNewMessage = {
                measurementsReceived++
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
        plant.needsWater.value = measurement.humidity < plant.minHumidity
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

    fun aPlantNeedsWater(): Boolean {
        for (plant in plantList) {
            if (plant.needsWater.value)
                return true
        }
        return false
    }

}
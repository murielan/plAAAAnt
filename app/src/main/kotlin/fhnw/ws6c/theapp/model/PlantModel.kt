package fhnw.ws6c.theapp.model

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.MqttService
import fhnw.ws6c.theapp.data.NotificationService
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
    private val notificationService: NotificationService
) {

    private val backgroundJob = SupervisorJob()
    private val modelScope = CoroutineScope(backgroundJob + Dispatchers.IO)



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
        notificationService.setupNotification()
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
            if (notify) notificationService.showAAAANotification(plant.name)
            plant.needsWater.value = true
            plantsThatNeedWaterList += plant
        } else if ((plant.needsWater.value == true || plant.needsWater.value == null) && measurement.humidity > plant.minHumidity) {
            if (notify) notificationService.showOKNotification(plant.name)
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



    private fun onFirebaseError(message: String) {
        notificationMessage = message
        firebaseError = true
    }

}
package fhnw.ws6c.theapp.model

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.NotificationService
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.data.defaultPlant
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

    // TODO reconnect
    fun resetConnectionFailure() {
        connectionFailed = false

    }

    fun getPlants() {
        isLoading = true
        modelScope.launch {
            firebaseService.getPlants(
                onSuccess = {
                    plantList = it
                    Log.e("Measurement", "Got the Plantlist, now trying measurements...")
                    getDbPlantMeasurements()
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


    private fun checkIfWaterNeeded(plant: Plant, measurement: Measurement, notify: Boolean) {
        // notification if needsWater changes
        if ((plant.needsWater.value == false || plant.needsWater.value == null) && measurement.humidity < plant.minHumidity) {
            Log.e("Notification", "should notify AAAAA")
            if (notify) notificationService.showAAAANotification(plant.name)
            plant.needsWater.value = true
            plantsThatNeedWaterList += plant
        } else if ((plant.needsWater.value == true || plant.needsWater.value == null) && measurement.humidity > plant.minHumidity) {
            Log.e("Notification", "should notify OK")
            if (notify) notificationService.showOKNotification(plant.name)
            plant.needsWater.value = false
            plantsThatNeedWaterList -= plant
        }
    }

    // get Measurements from Firebase and add to each known plant
    private fun getDbPlantMeasurements() {
        modelScope.launch {
            for (plant in plantList) {
                firebaseService.getDbPlantMeasurements(
                    plant.id,
                    onSuccess = {
                        var notify = false
                        if( plant.measurements.isNotEmpty()) {
                            notify = true
                            plant.measurements.clear()
                        }
                        plant.measurements.addAll(it)
                        // check with last Measurement if water is needed
                        if (plant.measurements.lastOrNull() != null) {
                            Log.e("Notification", "checking if notification needed")
                            checkIfWaterNeeded(plant, plant.measurements.last(), notify)
                        }
                    },
                    onFailure = {
                        onFirebaseError(it)
                    }
                )
            }
        }
    }

    fun countPlantsThatNeedWater(): Int = plantList.count { it.needsWater.value == true }



    private fun onFirebaseError(message: String) {
        notificationMessage = message
        firebaseError = true
    }

}
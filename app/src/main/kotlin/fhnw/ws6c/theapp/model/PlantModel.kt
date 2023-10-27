package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.MqttConnector
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.data.PlantRepository

class PlantModel(val plantRepo: PlantRepository) {
    var title = "PlAAAAnt"
    var boolean by mutableStateOf(true)
    var currentScreen by mutableStateOf(Screen.HOME)

    var plantList: List<Plant> = plantRepo.getPlants()

    private val mqttBroker = "broker.hivemq.com"
    private val topic = "fhnw/ws6/plaaaant"

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }

    private var notificationMessage by mutableStateOf("")

    var measurementsReceived by mutableIntStateOf(0)
    val allMeasurements = mutableListOf<Measurement>()

    fun connectAndSubscribe() {
        mqttConnector.connectAndSubscribe(topic = topic,
            onNewMessage = {
                measurementsReceived++
                allMeasurements.add(Measurement(it))
                addMeasurementToPlant(Measurement(it))
            },
            onError = { _, p ->
                notificationMessage = p
            })
    }

    private fun addMeasurementToPlant(measurement: Measurement) {
        for (plant in plantList) {
            if(plant.sensorId == measurement.sensorId){
                plant.measurements.add(measurement)
                break
            }
        }
    }

}
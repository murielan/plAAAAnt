package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.ws6c.theapp.data.MqttConnector

object TheModel {
    var title = "PlAAAAnt"
    var boolean by mutableStateOf(true)

    private const val mqttBroker = "broker.hivemq.com"
    private const val topic      = "fhnw/ws6/plaaaant"

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }

    private var notificationMessage by mutableStateOf("")
    var valuesReceived  by mutableIntStateOf(0)

    fun connectAndSubscribe(){
        mqttConnector.connectAndSubscribe(topic        = topic,
            onNewMessage = { valuesReceived++ },
            onError      = {_, p ->
                notificationMessage = p
            })
    }

}
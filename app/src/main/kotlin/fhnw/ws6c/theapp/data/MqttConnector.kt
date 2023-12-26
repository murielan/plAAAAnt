package fhnw.ws6c.theapp.data

import android.content.Context
import android.content.Intent
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class MqttConnector(private val context: Context,
                    mqttBroker: String,
                    private val qos: MqttQos = MqttQos.EXACTLY_ONCE){

    private val topic = "fhnw/ws6c/plaaaant"

    private val client = Mqtt5Client.builder()
        .serverHost(mqttBroker)
        //.serverPort(1884) //TODO change for Sensor Testing 1883
        .identifier(UUID.randomUUID().toString())
        .buildAsync()

    fun startForegroundService() {
        val intent = Intent(context, MqttService::class.java)
        context.startService(intent)
    }

    fun stopForegroundService() {
        val intent = Intent(context, MqttService::class.java)
        context.stopService(intent)
    }

    fun connectAndSubscribe(onNewMessage:       (JSONObject) -> Unit,
                            onError:            (Exception, String) -> Unit = {e, _ -> e.printStackTrace()},
                            onConnectionFailed: () -> Unit = {}) {
        client.connectWith()
            .cleanStart(true)
            .keepAlive(30)
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    onConnectionFailed()
                } else { //erst wenn die Connection aufgebaut ist, kann subscribed werden
                    subscribe(topic, onNewMessage, onError)
                }
            }
    }

    fun subscribe(topic:        String,
                  onNewMessage: (JSONObject) -> Unit,
                  onError:      (Exception, String) -> Unit = { e, _ -> e.printStackTrace() }){
        client.subscribeWith()
            .topicFilter(topic)
            .qos(qos)
            .noLocal(true)
            .callback {
                try {
                    onNewMessage(it.payloadAsJSONObject())
                }
                catch (e: Exception){
                    onError(e, it.payloadAsString())
                }
            }
            .send()
    }

    fun disconnect() {
        client.disconnectWith()
            .sessionExpiryInterval(0)
            .send()
    }

    companion object {
        fun disconnect() {
            disconnect()
        }
    }
}

// Extension Functions
private fun Mqtt5Publish.payloadAsJSONObject() : JSONObject = JSONObject(payloadAsString())
private fun Mqtt5Publish.payloadAsString() : String = String(payloadAsBytes, StandardCharsets.UTF_8)

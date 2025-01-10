//package fhnw.ws6c.theapp.data
//
//import android.content.Context
//import android.util.Log
//import com.hivemq.client.mqtt.datatypes.MqttQos
//import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
//import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
//import org.json.JSONObject
//import java.nio.charset.StandardCharsets
//import java.util.*
//
//class MqttConnector(
//    private val context: Context,
//    mqttBroker: String,
//    private val qos: MqttQos = MqttQos.EXACTLY_ONCE
//) {
//    private val client = Mqtt5Client.builder()
//        .serverHost(mqttBroker)
//        .identifier(UUID.randomUUID().toString())
//        .buildAsync()
//
//    fun connectAndSubscribe(
//        onNewMessage: (JSONObject) -> Unit,
//        onError: (Exception, String) -> Unit = { e, _ -> e.printStackTrace() },
//        onConnectionFailed: () -> Unit = {}
//    ) {
//        Log.d("notification", "connecting to broker...")
//        client.connectWith()
//            .cleanStart(true)
//            .keepAlive(30)
//            .send()
//            .whenComplete { ack, throwable ->
//                if (throwable != null) {
//                    Log.e("MqttConnector", "Failed to connect to the broker: ${throwable.message}")
//                    onConnectionFailed()
//                } else {
//                    // Subscribe after a successful connection
//                    Log.d("MqttConnector", "Connected to the broker: $ack")
//
//                    subscribe("app/plaaaant", onNewMessage, onError)
//                }
//            }
//        Log.d("notification", "connecting finished")
//    }
//
//    fun disconnect() {
//        client.disconnectWith().send()
//    }
//
//    private fun subscribe(
//        topic: String,
//        onNewMessage: (JSONObject) -> Unit,
//        onError: (Exception, String) -> Unit = { e, _ -> e.printStackTrace() }
//    ) {
//        Log.d("notification", "subscribing...")
//        client.subscribeWith()
//            .topicFilter(topic)
//            .qos(qos)
//            .callback {
//                try {
//                    Log.d("MqttConnector", "Message received: ${it.payloadAsString()}")
//                    onNewMessage(it.payloadAsJSONObject())
//                } catch (e: Exception) {
//                    Log.e("MqttConnector", "Error handling message: ${e.message}")
//                    onError(e, it.payloadAsString())
//                }
//            }
//            .send()
//            .whenComplete { subAck, throwable ->
//                if (throwable != null) {
//                    Log.e("MqttConnector", "Failed to subscribe to $topic: ${throwable.message}")
//                } else {
//                    Log.d("MqttConnector", "Subscribed to topic $topic: $subAck")
//                }
//            }
//    }
//}
//
//// Extension Functions
//private fun Mqtt5Publish.payloadAsJSONObject(): JSONObject = JSONObject(payloadAsString())
//private fun Mqtt5Publish.payloadAsString(): String =
//    String(payloadAsBytes, StandardCharsets.UTF_8)
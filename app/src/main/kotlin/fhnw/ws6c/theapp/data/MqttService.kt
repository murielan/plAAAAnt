package fhnw.ws6c.theapp.data


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fhnw.ws6c.theapp.receiver.MqttStaticReceiver
import org.json.JSONObject


class MqttService : Service() {
    private lateinit var mqttConnector: MqttConnector

    override fun onCreate() {
        super.onCreate()
        mqttConnector = MqttConnector(this, BROKER_URL)
        Log.d(TAG, "MqttService created")

        // Start MQTT Connection
        connectToBroker()
    }

    private fun connectToBroker() {
        mqttConnector.connectAndSubscribe(
            onNewMessage = { message ->
                Log.d(TAG, "Received MQTT message: $message")
                sendNewMessageBroadcast(message)
            },
            onError = { exception, payload ->
                Log.e(TAG, "MQTT error: ${exception.message}, payload: $payload", exception)
                sendConnectionFailedBroadcast("Error: $payload")
            },
            onConnectionFailed = {
                Log.e(TAG, "Connection to MQTT broker failed")
                sendConnectionFailedBroadcast("Connection failed.")
            }
        )
    }

    private fun sendNewMessageBroadcast(message: JSONObject) {
        val intent = Intent().apply {
            action = MqttStaticReceiver.ACTION_NEW_MESSAGE
            putExtra(MqttStaticReceiver.EXTRA_MESSAGE, message.toString())
            setClassName(this@MqttService, "fhnw.ws6c.theapp.receiver.MqttStaticReceiver")
        }
        Log.d(TAG, "Broadcasting new message: ${intent.action} with data: $message")
        sendBroadcast(intent)
    }

    private fun sendConnectionFailedBroadcast(error: String) {
        val intent = Intent().apply {
            action = MqttStaticReceiver.ACTION_CONNECTION_FAILED
            putExtra(MqttStaticReceiver.EXTRA_ERROR, error)
            setClassName(this@MqttService, "fhnw.ws6c.theapp.receiver.MqttStaticReceiver")
        }
        Log.d(TAG, "Broadcasting connection failed: ${intent.action} with error: $error")
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MqttService destroyed, disconnecting MQTT...")
        mqttConnector.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TAG = "MqttService"
        private const val BROKER_URL = "broker.hivemq.com"
        const val ACTION_NEW_MESSAGE = "fhnw.ws6c.ACTION_NEW_MESSAGE"
        const val ACTION_CONNECTION_FAILED = "fhnw.ws6c.ACTION_CONNECTION_FAILED"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        const val EXTRA_ERROR = "EXTRA_ERROR"
    }
}
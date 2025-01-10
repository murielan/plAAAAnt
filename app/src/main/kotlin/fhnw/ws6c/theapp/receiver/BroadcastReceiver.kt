//package fhnw.ws6c.theapp.receiver
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import fhnw.ws6c.theapp.data.Measurement
//import fhnw.ws6c.theapp.model.PlantModel
//import org.json.JSONObject
//import java.lang.ref.WeakReference
//
//class MqttStaticReceiver : BroadcastReceiver() {
//
//    companion object {
//        private var plantModelRef: WeakReference<PlantModel>? = null
//
//        fun setPlantModel(plantModel: PlantModel) {
//            plantModelRef = WeakReference(plantModel)
//        }
//        const val ACTION_NEW_MESSAGE = "fhnw.ws6c.ACTION_NEW_MESSAGE"
//        const val ACTION_CONNECTION_FAILED = "fhnw.ws6c.ACTION_CONNECTION_FAILED"
//        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
//        const val EXTRA_ERROR = "EXTRA_ERROR"
//    }
//
//
//    override fun onReceive(context: Context, intent: Intent) {
//        Log.d("MqttStaticReceiver", "Broadcast received with action: ${intent.action}")
//        val plantModel = plantModelRef?.get()
//        when (intent.action) {
//            ACTION_NEW_MESSAGE -> {
//                val messageJson = intent.getStringExtra(EXTRA_MESSAGE)
//                if (messageJson != null) {
//                    Log.d("MqttStaticReceiver", "New message received: $messageJson")
//                    try {
//                        val measurement = Measurement(JSONObject(messageJson))
//                        Log.d("MqttStaticReceiver", "Parsed Measurement: $measurement")
////                        plantModel?.addMeasurementToPlant(measurement) // Forward to PlantModel
//                    } catch (e: Exception) {
//                        Log.e("MqttStaticReceiver", "Error parsing measurement: ${e.message}")
//                    }
//                }
//            }
//            ACTION_CONNECTION_FAILED -> {
//                val error = intent.getStringExtra(EXTRA_ERROR)
//                Log.e("MqttStaticReceiver", "Connection failed: $error")
//            }
//        }
//    }
//}
package fhnw.ws6c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.MqttConnector
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.ui.PlAAAAntUI


class MainActivity : ComponentActivity() {
    private lateinit var model : PlantModel
    private lateinit var mqttConnector: MqttConnector
    private lateinit var firebaseService: FirebaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: correct naming of app and all files, namespace, applicationId etc. (do not use TheUi, TheModel, TheApp etc.) - check gradle files!

        mqttConnector = MqttConnector(this, "broker.hivemq.com")
        mqttConnector.startForegroundService()

        firebaseService = FirebaseService()

        model = PlantModel(this, mqttConnector, firebaseService)
        model.getPlants()
        model.connectAndSubscribe()

        setContent {
            PlAAAAntUI(model)
        }
    }

    /**
     * Eine der Activity-LiveCycle-Methoden als Beispiel, falls da weiteres benötigt wird.
     */
    override fun onStop() {
        super.onStop()
        //anything else?
        mqttConnector.stopForegroundService()
        print("stopped")
    }
}
package fhnw.ws6c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.MqttConnector
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.ui.PlAAAAntUI
import fhnw.ws6c.theapp.model.AuthModel
import fhnw.ws6c.theapp.model.Screen


class MainActivity : ComponentActivity() {
    private lateinit var model : PlantModel
    private lateinit var mqttConnector: MqttConnector
    private lateinit var firebaseService: FirebaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: correct naming of app and all files, namespace, applicationId etc. (do not use TheUi, TheModel, TheApp etc.) - check gradle files!
        installSplashScreen()

        firebaseService = FirebaseService()

        mqttConnector = MqttConnector(this, "broker.hivemq.com")
        mqttConnector.startForegroundService()

        model = PlantModel(this, mqttConnector, firebaseService)
        model.getPlants()
        model.connectAndSubscribe()

        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        if (isLoggedIn) {
            model.currentScreen = Screen.HOME
        } else {
            model.currentScreen = Screen.SIGNUP
        }

        setContent {
            val authModel: AuthModel = viewModel {
                AuthModel(model)
            }
            PlAAAAntUI(authModel, model)
        }
    }

    /**
     * Eine der Activity-LiveCycle-Methoden als Beispiel, falls da weiteres benötigt wird.
     */
    override fun onStop() {
        super.onStop()
        // anything else?
        mqttConnector.stopForegroundService()
        print("stopped")
    }
}
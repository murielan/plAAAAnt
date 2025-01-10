package fhnw.ws6c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.NotificationService
import fhnw.ws6c.theapp.model.AuthModel
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.ui.PlAAAAntUI


class MainActivity : ComponentActivity() {
    private lateinit var model : PlantModel
    private lateinit var firebaseService: FirebaseService
    private lateinit var notificationService: NotificationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: correct naming of app and all files, namespace, applicationId etc. (do not use TheUi, TheModel, TheApp etc.) - check gradle files!
        installSplashScreen()

        firebaseService = FirebaseService()
        notificationService = NotificationService(this)
        notificationService.setupNotification()


        model = PlantModel(this, firebaseService, notificationService)
        model.getPlants()

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
//        model.onCleared()
//        mqttConnector.stopForegroundService()
        print("stopped")
    }
}
package fhnw.ws6c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fhnw.ws6c.theapp.model.TheModel
import fhnw.ws6c.theapp.ui.AppUI


class MainActivity : ComponentActivity() {
    private lateinit var model : TheModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: correct naming of app and all files, namespace, applicationId etc. (do not use TheUi, TheModel, TheApp etc.) - check gradle files!
        model = TheModel

        setContent {
            AppUI(model)
        }
    }

    /**
     * Eine der Activity-LiveCycle-Methoden als Beispiel, falls da weiteres benötigt wird.
     */
    override fun onStop() {
        super.onStop()
        //anything else?
        print("stopped")
    }
}
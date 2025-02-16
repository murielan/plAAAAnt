package fhnw.ws6c.theapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fhnw.ws6c.theapp.data.FirebaseService
import fhnw.ws6c.theapp.data.MqttConnector
import fhnw.ws6c.theapp.model.PlantModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantModelInstrumentedTest {

    private lateinit var plantModel: PlantModel

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val mqttConnector = MqttConnector(context, "broker.hivemq.com")
        val firebaseService = FirebaseService()
        plantModel = PlantModel(context, mqttConnector, firebaseService)
    }

    @Test
    fun testConnectAndSubscribe() {
        // test logic for connectAndSubscribe
    }

    @Test
    fun testGetPlants() {
        // test logic for getPlants
    }

    // And so on

}
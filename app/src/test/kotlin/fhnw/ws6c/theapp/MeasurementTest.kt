package fhnw.ws6c.theapp

import fhnw.ws6c.theapp.data.Measurement
import fhnw.ws6c.theapp.data.getTime
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class MeasurementTest {

    @Test
    fun testAsHashMap() {
        val jsonString = """{
            "sensorId": 1,
            "humidity": 50,
            "time": 1697804398896
        }"""
        val measurement = Measurement(JSONObject(jsonString))

        val expectedHashMap = hashMapOf(
            "sensorId" to 1,
            "humidity" to 50,
            "time" to getTime(1697804398896)
        )

        assertEquals(1, measurement.sensorId)
        assertEquals(50, measurement.humidity)
        // TODO fix test time
    }

    @Test
    fun testConstructorWithJsonString() {
        val jsonString = """{
            "sensorId": 2,
            "humidity": 75,
            "time": 1697804398896
        }"""

        val measurement = Measurement(jsonString)

        assertEquals(2, measurement.sensorId)
        assertEquals(75, measurement.humidity)
        // TODO fix test time
    }

    @Test
    fun testDefaultConstructor() {
        val measurement = Measurement()

        assertNotNull(measurement.sensorId)
        assertEquals(0, measurement.humidity)
        assertNotNull(measurement.time)
    }

    @Test
    fun testGetTime() {
        val timestamp = 1697804398896
        val expectedFormattedTime = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(Date(timestamp))

        assertEquals(expectedFormattedTime, getTime(timestamp))
    }
}
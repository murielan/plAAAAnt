package fhnw.ws6c.theapp.data

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

// { "sensorId": 4, "humidity": 652, "time": 1697804398896}
data class Measurement(val json: JSONObject) {
    fun asHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "sensorId" to sensorId,
            "humidity" to humidity,
            "time" to time
        )
    }

    val sensorId: Int = json.optInt("sensorId", 0)
    val humidity: Int = json.optInt("humidity", 0)
    val time: String = getTime(System.currentTimeMillis())
    // TODO get it from the sensor:  getTime(json.optLong("time",0L))

    constructor(jsonString: String) : this(JSONObject(jsonString))

    //constructor with no arguments for serialization
    constructor() : this(
        JSONObject(
            """{
                "sensorId" : ${Random.nextInt()},
                "humidity" : ${0},
                "time" : ${0L}
            }""".trimMargin()
        )
    )
}

fun getTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
package fhnw.ws6c.theapp.data

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class Measurement(
    var sensorId: Int = 0,
    var humidity: Int = 0,
    var time: String
) {

    // { "sensorId": 4, "humidity": 652, "time": 1697804398896}
    constructor(json: JSONObject) : this(
        sensorId = json.getInt("sensorId"),
        humidity = json.getInt("humidity"),
        time = getTime(System.currentTimeMillis()) // TODO get it from the sensor: json.getLong("time")
    )
    constructor(linkedHash: LinkedHashMap<String, Any>) : this(
        sensorId = linkedHash["sensorId"] as Int,
        humidity = linkedHash["humidity"] as Int,
        time = getTime(linkedHash["time"] as Long)
    )
}
fun getTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}




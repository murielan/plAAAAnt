package fhnw.ws6c.theapp.data

import org.json.JSONObject
import java.sql.Timestamp

data class Measurement(
    val sensorId: Int = 0,
    val humidity: Int = 0,
    val time: Timestamp
) {
    // { "sensorId": 4, "humidity": 652, "time": 1697804398896}
    constructor(json: JSONObject) : this(
        sensorId = json.getInt("sensorId"),
        humidity = json.getInt("humidity"),
        time = Timestamp(json.getLong("time")) // milliseconds since January 1, 1970, 00:00:00 GMT
    )
    constructor(linkedHash: LinkedHashMap<String, Any>) : this(
        sensorId = linkedHash["sensorId"] as Int,
        humidity = linkedHash["humidity"] as Int,
        time = Timestamp(linkedHash["time"] as Long)
    )
}

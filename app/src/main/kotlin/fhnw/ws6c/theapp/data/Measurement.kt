package fhnw.ws6c.theapp.data

import org.json.JSONObject
import java.sql.Timestamp

data class Measurement(
    val sensorId: Int = 0,
    val humidity: Int = 0,
    val time: Timestamp
) {
    constructor(json: JSONObject) : this(
        sensorId = json.getInt("sensorId"),
        humidity = json.getInt("humidity"),
        time = Timestamp(json.getLong("time"))
    )
}

package fhnw.ws6c.theapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.json.JSONObject
import java.sql.Timestamp

data class Plant(val json: JSONObject) {
    val id = json.getInt("id")
    val name = json.getString("name")
    val place = json.getString("place")
    val birthday = Timestamp(json.getLong("birthday"))
    val minHumidity = json.getInt("minHumidity")
    val picture = json.getString("picture") // TODO: implement picture
    val sensorId = json.getInt("sensorId")
    var measurements = mutableListOf<Measurement>()
    var needsWater: MutableState<Boolean> = mutableStateOf(false)

    constructor(jsonString: String) : this(JSONObject(jsonString))
}


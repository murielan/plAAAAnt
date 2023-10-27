package fhnw.ws6c.theapp.data

import org.json.JSONObject
import java.sql.Timestamp

class Plant(json: JSONObject) {
    val id = json.getInt("id")
    val name = json.getString("name")
    val place = json.getString("place")
    val birthday = Timestamp(json.getLong("birthday"))
    val picture = json.getString("picture") //TODO: implement picture
    val sensorId = json.getInt("sensorId")
    val measurements = mutableListOf <Measurement>()

    constructor(jsonString: String) : this(JSONObject(jsonString))
}


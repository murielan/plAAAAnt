package fhnw.ws6c.theapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import fhnw.ws6c.R
import org.json.JSONObject

data class Plant(val json: JSONObject) {
    val id = json.getInt("id")
    val name = json.getString("name")
    val place = json.getString("place")
    val birthday = json.getLong("birthday")
    val minHumidity = json.getInt("minHumidity")
    val pictureHappy = getPicture(json.getInt("picture"), true)
    val pictureSad= getPicture(json.getInt("picture"), false)
    val sensorId = json.getInt("sensorId")
    var measurements = mutableListOf<Measurement>()
    var needsWater: MutableState<Boolean> = mutableStateOf(false)

    constructor(jsonString: String) : this(JSONObject(jsonString))
}

private fun getPicture(plant: Int, happy: Boolean): Int {
    if (plant == 1 && happy)
    {
        return R.drawable.aloe_happy
    } else if (plant == 1) {
        return R.drawable.aloe_sad
    } else if (plant == 2 && happy)
    {
        return R.drawable.mons_happy
    } else if (plant == 2) {
        return R.drawable.mons_sad
    } else if (plant == 3 && happy)
    {
        return R.drawable.cact_happy
    } else if (plant == 3) {
        return R.drawable.cact_sad
    }

    return R.drawable.aloe_sad
}
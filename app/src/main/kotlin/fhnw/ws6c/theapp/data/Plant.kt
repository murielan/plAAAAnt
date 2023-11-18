package fhnw.ws6c.theapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import fhnw.ws6c.R
import org.json.JSONObject
import kotlin.random.Random

data class Plant(val json: JSONObject) {
    //data from firebase
    val id: Int = json.optInt("id", Random.nextInt())
    val name: String = json.optString("name", "nA")
    val place: String = json.optString("place", "nA")
    val birthday: Long = json.optLong("birthday", -1L)
    val minHumidity: Int = json.optInt("minHumidity", -1)
    var pictureHappy: Int = getPicture( json.optInt("picture", 3), true)
    val pictureSad: Int = getPicture( json.optInt("picture", 3), false)
    val sensorId: Int = json.optInt("sensorId", 0)
    var measurements = mutableListOf<Measurement>()
    var needsWater: MutableState<Boolean> = mutableStateOf(false)

    constructor(jsonString: String) : this(JSONObject(jsonString))

    //constructor with no arguments for serialization
    constructor(): this(JSONObject(
        """{
                |"id" : ${Random.nextInt()},
                |"name" : "",
                |"place" : "",
                |"birthday" : 0,
                |"minHumidity" : 0,
                |"picture" : 0,
                |"sensorId" : 0
            }""".trimMargin()
    ))

    companion object {
        // Create a default plant instance with default values
        val defaultPlant = Plant( JSONObject("""
            {
                "id": 0,
                "name": "default",
                "place": "default",
                "birthday": "1697804398896",
                "minHumidity" : 20,
                "picture": 0,
                "sensorId": 0
              }
        """))
    }
}

public fun getPicture(plant: Int, happy: Boolean): Int {
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
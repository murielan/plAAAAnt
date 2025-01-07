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
    val picture: Int = json.optInt("picture", 3)
    val sensorId: Int = json.optInt("sensorId", 0)
    var measurements = mutableListOf<Measurement>()
    var needsWater: MutableState<Boolean?> = mutableStateOf(null)

    constructor(jsonString: String) : this(JSONObject(jsonString))

    //constructor with no arguments for serialization
    constructor() : this(
        JSONObject(
            """{
                "id" : ${Random.nextInt()},
                "name" : "",
                "place" : "",
                "birthday" : 0,
                "minHumidity" : 0,
                "picture" : 3,
                "sensorId" : 0
            }""".trimMargin()
        )
    )

    fun getPictureDrawable(): Int {
        if (picture == 1 && needsWater.value == false) {
            return R.drawable.aloe_happy
        } else if (picture == 1 && needsWater.value == true) {
            return R.drawable.aloe_sad
        } else if (picture == 2 && needsWater.value == false) {
            return R.drawable.mons_happy
        } else if (picture == 2 && needsWater.value == true) {
            return R.drawable.mons_sad
        } else if (picture == 3 && needsWater.value == false) {
            return R.drawable.cact_happy
        } else if (picture == 3 && needsWater.value == true) {
            return R.drawable.cact_sad
        }
        return R.drawable.aloe_sad
    }
}

// Create a default plant instance with default values
fun defaultPlant() = Plant(
    JSONObject(
        """
            {
                "id": 0,
                "name": "default",
                "place": "default",
                "birthday": "1697804398896",
                "minHumidity" : 20,
                "picture": 0,
                "sensorId": 0
              }
        """
    )
)
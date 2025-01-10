package fhnw.ws6c.theapp.data

import com.google.firebase.Timestamp
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


data class Measurement(
    val sensorId: Int = 0,
    var humidity: Int = 0,
    var time: Long = 0L,
    val timestamp: Timestamp? = null
) {

    // Format "time" (Long) to readable string
    fun formattedTime(): String {
        return if (time > 0) {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            sdf.format(Date(time))
        } else {
            "Invalid time"
        }
    }

    // Format "timestamp" (Firestore Timestamp) to readable string
    fun formattedTimestamp(): String {
        return timestamp?.toDate()?.let { date ->
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            sdf.format(date)
        } ?: "Invalid timestamp"
    }

    // Firestore-compatible HashMap
    fun asHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "sensorId" to sensorId,
            "humidity" to humidity,
            "time" to time,
            "timestamp" to (timestamp ?: Timestamp.now()) // Use current timestamp if null
        )
    }

    // Alternative constructor for parsing JSON
    constructor(json: JSONObject) : this(
        sensorId = json.optInt("sensorId", 0),
        humidity = json.optInt("humidity", 0),
        time = json.optLong("time", 0L),
        timestamp = json.optLong("timestamp", 0L).takeIf { it > 0 }?.let { Timestamp(Date(it)) }
    )
}

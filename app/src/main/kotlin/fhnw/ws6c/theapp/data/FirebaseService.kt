package fhnw.ws6c.theapp.data

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class FirebaseService {

    private val db = Firebase.firestore
    private var plantRef = db.collection("plants")
    private var newPlants = mutableListOf<Plant>()
    private var newMeasurements = mutableListOf<Measurement>()

    fun getPlants(onSuccess: (plants: List<Plant>)->Unit, onFailure: (error: String)->Unit) {
        Log.d("notification", "Trying to get plants...")
        plantRef.get()
            .addOnSuccessListener { result ->
                run {
                    newPlants = mutableListOf()
                    for (document in result) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        val plant = document.toObject<Plant>()
                        newPlants.add(plant)
                    }
                    onSuccess.invoke(newPlants)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching plants: ${exception.message}", exception)
                onFailure("Error fetching plants")
            }
    }

    fun getDbPlantMeasurements(plantId: Int, onSuccess: (measurements: List<Measurement>)->Unit, onFailure: (error: String) -> Unit) {
        db.collection("plants")
            .document(plantId.toString())
            .collection("measurements")
            .orderBy("time", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                newMeasurements = mutableListOf()
                for (document in result) {
                    val measurement = document.toObject<Measurement>()
                    newMeasurements.add(measurement)
                }
                onSuccess.invoke(newMeasurements)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
                //TODO: inform user ?
                onFailure("Error getting measurements")
            }
    }

    // if MQTT client receives measurement, add it to firebase and refresh measurements
    fun addMeasurementToPlant(measurement: Measurement, onFailure: (error: String) -> Unit) {
        val plantId = measurement.sensorId

        db.collection("plants")
            .document(plantId.toString())
            .collection("measurements")
            .add(measurement.asHashMap())
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding measurement", e)
                // TODO: inform user ?
                onFailure("Error adding measurements to plant")
            }
    }
}
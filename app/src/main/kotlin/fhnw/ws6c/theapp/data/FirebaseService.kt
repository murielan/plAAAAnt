package fhnw.ws6c.theapp.data

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class FirebaseService {

    private val db = Firebase.firestore
    private var plantRef = db.collection("plants")
    private var newPlants = mutableListOf<Plant>()
    private var newMeasurements = mutableListOf<Measurement>()

    fun getPlants(onSuccess: (plants: List<Plant>)->Unit) {
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
            }
    }

    fun getDbMeasurements(onSuccess: (measurements: List<Measurement>)->Unit) {
        db.collection("measurements")
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
            }
    }

    // if MQTT client receives measurement, add it to firebase and refresh measurements
    fun addMeasurementToPlant(measurement: Measurement) {
        db.collection("measurements")
            .add(measurement)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding measurement", e)
                //TODO: inform user ?
            }
    }
}
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

    fun getPlants(onSuccess: (plants: List<Plant>)->Unit, onFailure: (error: String)->Unit) {
        plantRef.get()
            .addOnSuccessListener { result ->
                run {
                    val newPlants = mutableListOf<Plant>()
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
        plantRef
            .document(plantId.toString())
            .collection("measurements")
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(ContentValues.TAG, "Error listening for changes.", exception)
                    onFailure("Error listening for measurements")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val newMeasurements = mutableListOf<Measurement>()
                    for (document in snapshot.documents) {
                        val measurement = document.toObject<Measurement>()
                        if (measurement != null) {
                            newMeasurements.add(measurement)
                        }
                    }
                    onSuccess(newMeasurements)
                } else {
                    onFailure("Snapshot is null")
                }
            }
    }

    // TODO: change Plant on firebase

}
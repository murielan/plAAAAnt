package fhnw.ws6c.theapp.data

import android.content.Context
import org.json.JSONArray
import java.io.InputStream
import java.nio.charset.StandardCharsets

class PlantRepository {
    lateinit var data: List<Plant>

    fun loadPlants(context: Context) {
        val plants = mutableListOf<Plant>()

        val json = JSONArray(loadFromAsset(context, "plantlist.json"))

        for (i in 0 until json.length()) {
            plants.add(Plant(json.getJSONObject(i)))
        }

       data = plants
    }

    fun getPlants(): List<Plant> {
        return data
    }

    private fun loadFromAsset(context: Context, fileName: String): String {
        val inputStream: InputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        return String(buffer, StandardCharsets.UTF_8)
    }

}

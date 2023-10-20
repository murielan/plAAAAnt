package fhnw.ws6c.theapp.data

import android.graphics.Bitmap
import java.util.Date

data class Plant( val id: Int = 0,
    val name: String = "",
    val place: String = "",
    val birthday: Date,
    val picture: Bitmap,
    val measurements: Measurement)

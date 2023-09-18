package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object TheModel {
    var title = "Hello ws6C"
    var boolean by mutableStateOf(true)
}
package fhnw.ws6c.theapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val title: String, val icon: ImageVector) {
    SIGNUP("Sign Up", Icons.Filled.Home),
    SIGNIN("Sign In", Icons.Filled.Home),
    HOME("PlAAAAnts", Icons.Filled.Home),
    PLANT("", Icons.Filled.Home),
    AAAA("AAAA!!", Icons.Filled.Warning)
}
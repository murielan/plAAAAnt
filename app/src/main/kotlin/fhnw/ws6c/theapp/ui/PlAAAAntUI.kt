package fhnw.ws6c.theapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.ui.screens.HomeScreen

@Composable
fun PlAAAAntUI(model: PlantModel) {
    with(model) {
        Crossfade(targetState = currentScreen, label = "") { screen ->
            when {
                (Screen.HOME == screen) -> {
                    HomeScreen(model)
                }
                // (Screen.Plant == screen) -> { PlantScreen(model)}
                // (Screen.AAAA == screen) -> { PlantScreen(model)}
            }
        }
    }
}
package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.model.Screen

@Composable
fun NavigationBottomAppBar(model: PlantModel) {
    with(model) {
        NavigationBar(
        ) {
            Screen.entries.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, screen.title) },
                    label = { Text(screen.title) },
                    selected = currentScreen == screen,
                    onClick = { currentScreen = screen }
                )
            }
        }
    }
}

@Composable
fun PlantBox(model: PlantModel, plant: Plant) {
    with(model) {
        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(0.49f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Green)
        ) {
            Column {
                Text(text = plant.name)
                Text(text = "Humidity: " + plant.measurements.lastOrNull()?.humidity)
            }
        }
    }
}



package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.model.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(model: PlantModel) {
    with(model) {
        if (currentScreen != Screen.PLANT) {
            TopAppBar(
                title = {
                    Text(
                        currentScreen.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 32.sp
                    )
                }
            )
        } else {
            TopAppBar(
                title = {
                    Text(
                        currentPlant.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 32.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { currentScreen = Screen.HOME }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBottomAppBar(model: PlantModel) {
    with(model) {
        Box {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                Screen.entries.forEach { screen ->
                    if (screen != Screen.PLANT) {
                        NavigationBarItem(
                            icon = {
                                if(aPlantNeedsWater() && screen.icon == Screen.AAAA.icon) {
                                    BadgedBox(badge = { Badge {  } }) {
                                        Icon(screen.icon, screen.title)
                                    }
                                } else Icon(screen.icon, screen.title)
                            },
                            label = { Text(screen.title) },
                            selected = currentScreen == screen,
                            onClick = { currentScreen = screen },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                indicatorColor = Color.White
                            ),
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
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
                .background(
                    if (plant.needsWater.value) {
                        MaterialTheme.colorScheme.tertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    }
                )
                .clickable {
                    currentPlant = plant
                    currentScreen = Screen.PLANT
                }
        ) {
            Column {
                Text(text = plant.name)
                Text(text = "Humidity: " + plant.measurements.lastOrNull()?.humidity)
            }
        }
    }
}




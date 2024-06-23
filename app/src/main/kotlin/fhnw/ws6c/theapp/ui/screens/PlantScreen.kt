package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.model.AuthModel
import fhnw.ws6c.theapp.model.PlantModel

@Composable
fun PlantScreen(model: PlantModel, authModel: AuthModel) {
    with(model) {
        Scaffold(
            topBar = { NavigationTopAppBar(model, authModel) },
            content = { innerPadding -> SinglePlantContent(currentPlant, innerPadding) },
            bottomBar = { NavigationBottomAppBar(model) }
        )
    }
}

@Composable
fun SinglePlantContent(currentPlant: Plant, innerPadding: PaddingValues) {
    with(currentPlant)
    {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .scrollable(
                    orientation = Orientation.Vertical,
                    // Scrollable state: describes how to consume
                    // scrolling delta and update offset
                    state = rememberScrollableState { delta ->
                        delta
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                PlantEmotion(currentPlant)
                InfoAboutPlant(currentPlant)
            }
        }
    }
}

@Composable
fun PlantEmotion(currentPlant: Plant) {
    with(currentPlant) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (needsWater.value) {
                SadPlant(currentPlant)
            } else {
                HappyPlant(currentPlant)
            }
        }
    }
}

@Composable
fun InfoAboutPlant(currentPlant: Plant) {
    with(currentPlant) {
        Box(modifier = Modifier.padding(0.dp, 20.dp)) {
            Row {
                Column(Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
                    Icon(Icons.Filled.Place, contentDescription = "Place")
                }
                Column {
                    Text(
                        text = place.ifEmpty { "unbekannt" }
                    )
                }
            }
        }
        Box {
            Row {
                Column(Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
                    Icon(Icons.Outlined.MonitorHeart, contentDescription = "Alive")
                }
                Column {
                    val lastMeasurement = measurements.lastOrNull()
                    if (lastMeasurement != null) {
                        Text(text = "Bodenfeuchtigkeit: ${lastMeasurement.humidity}%")
                        Text(text = "Gemessen am: ${lastMeasurement.time}")
                    } else {
                        Text(text = "Bodenfeuchtigkeit: unbekannt")
                    }
                }
            }
        }
    }
}

@Composable
fun HappyPlant(currentPlant: Plant) {
    with(currentPlant) {
        Image(
            painter = painterResource(R.drawable.chat_happy),
            contentDescription = "Mir gehts super!",
            modifier = Modifier
                .fillMaxWidth()
        )
        Image(
            painter = painterResource(getPictureDrawable()),
            contentDescription = "Plant Image",
            modifier = Modifier
                .size(200.dp)
                .scale(1F)
        )
    }
}

@Composable
fun SadPlant(currentPlant: Plant) {
    with(currentPlant) {
        Image(
            painter = painterResource(R.drawable.chat_sad),
            contentDescription = "AAAA!! Ich verdurste, bitte hilf mir!!",
            modifier = Modifier
                .fillMaxWidth()
        )
        Image(
            painter = painterResource(getPictureDrawable()),
            contentDescription = "Plant Image",
            modifier = Modifier
                .size(200.dp)
                .scale(1F)
        )
    }
}
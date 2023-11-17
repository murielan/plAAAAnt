package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.model.PlantModel

@Composable
fun AAAAScreen(model: PlantModel) {
    with(model) {
        Scaffold(
            topBar = { NavigationTopAppBar(model) },
            content = { innerPadding -> AAAAContent(model, innerPadding) },
            bottomBar = { NavigationBottomAppBar(model) }
        )
    }
}

@Composable
fun AAAAContent(model: PlantModel, innerPadding: PaddingValues) {
    with(model)
    {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                if (counterPlantsThatNeedWater()>0) {
                    PlantBoxes(model)
                } else {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No plant is screaming")
                        Text("You have a green thumb!")
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlantBoxes(model: PlantModel) {
    with(model) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            maxItemsInEachRow = 2
        ) {
            for (plant in plantList) {
                if (plant.needsWater.value) {
                    PlantBox(model, plant)
                }
            }
        }
    }
}

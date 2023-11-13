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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.model.PlantModel

@Composable
fun HomeScreen(model: PlantModel) {
    with(model) {
        Scaffold(
            topBar = { NavigationTopAppBar(model)},
            content = { innerPadding -> HomeContent(model, innerPadding) },
            bottomBar = { NavigationBottomAppBar(model) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeContent(model: PlantModel, innerPadding: PaddingValues) {
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
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    maxItemsInEachRow = 2
                ) {
                    for (plant in plantList) {
                        PlantBox(model, plant)
                    }
                }
            }
        }
    }
}







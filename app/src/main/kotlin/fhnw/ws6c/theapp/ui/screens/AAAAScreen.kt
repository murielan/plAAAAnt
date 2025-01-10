package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.AuthModel
import fhnw.ws6c.theapp.model.PlantModel

@Composable
fun AAAAScreen(model: PlantModel, authModel: AuthModel) {
    with(model) {
        Scaffold(
            topBar = { NavigationTopAppBar(model, authModel) },
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
            if (countPlantsThatNeedWater() > 0) {
                AAAAPlants(model)
            } else {
                AllGood()
            }
        }
    }
}

@Composable
fun AAAAPlants(model: PlantModel) {
    with(model) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plantsThatNeedWaterList) { item ->
                PlantBox(model = model, plant = item)
            }
        }
    }
}

@Composable
fun AllGood() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.aloe_happy),
            contentDescription = "Sad Plant",
            modifier = Modifier
                .size(120.dp)
                .padding(0.dp, 20.dp)
        )
        Text("Deinen PlAAAAnts geht es super!")
    }
}

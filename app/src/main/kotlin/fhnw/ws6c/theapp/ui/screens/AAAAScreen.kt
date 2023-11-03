package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.model.PlantModel

@Composable
fun AAAAScreen(model: PlantModel) {
    with(model) {
        Scaffold(
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
                Text(
                    text = "AAAA!!",
                    style = TextStyle(fontSize = 36.sp)
                )
            }
        }
    }
}
package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.model.PlantModel

@Composable
fun HomeScreen(model: PlantModel) {
    with(model){
        Column(
            modifier         = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ){
            Text(text  = "PlAAAAnt",
                style = TextStyle(fontSize = 36.sp)
            )
            /*Button(onClick = { boolean = !boolean }) {
                Text(text = "Material 3 Button")
            }
            Switch(checked = boolean, onCheckedChange = { boolean = !boolean })
            */
            Text(text  = "Values received: $measurementsReceived",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(vertical = 20.dp)
            )
            if(allMeasurements.isNotEmpty()) {
                Text(text = "For the sensor ${allMeasurements.last().sensorId} a humidity of " +
                        "${allMeasurements.last().humidity} has been measured at ${allMeasurements.last().time.toString()}")
            }
            if(plantList.isNotEmpty()){
                Text(text = plantList[0].name + ": " + plantList[0].measurements.lastOrNull()?.humidity)
                Text(text = plantList[1].name + ": " + plantList[1].measurements.lastOrNull()?.humidity)
                Text(text = plantList[2].name + ": " + plantList[2].measurements.lastOrNull()?.humidity)

            }
        }
    }
}
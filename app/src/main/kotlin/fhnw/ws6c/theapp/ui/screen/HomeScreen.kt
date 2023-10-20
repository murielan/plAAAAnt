package fhnw.ws6c.theapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.model.TheModel

@Composable
fun HomeScreen(model: TheModel) {
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
                style = TextStyle(fontSize = 14.sp)
            )
            if(allMeasurements.isNotEmpty()) {
                Text(text = "${allMeasurements.last().humidity}")
            }
        }
    }
}
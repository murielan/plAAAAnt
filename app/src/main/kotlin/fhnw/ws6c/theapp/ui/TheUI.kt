package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.model.TheModel

@Composable
fun AppUI(model : TheModel){
    with(model){
            Column(
                modifier         = Modifier.fillMaxSize().padding(20.dp)
            ){
                Text(text  = "ws6c - app with Material 3",
                    style = TextStyle(fontSize = 36.sp)
                )
                Button(onClick = { boolean = !boolean }) {
                    Text(text = "Material 3 Button")
                }
                Switch(checked = boolean, onCheckedChange = { boolean = !boolean })
                Text(text  = "Values received: $valuesReceived",
                    style = TextStyle(fontSize = 14.sp)
                )
            }
    }
}
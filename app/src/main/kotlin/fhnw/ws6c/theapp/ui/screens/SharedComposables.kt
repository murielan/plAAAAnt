package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.data.Plant
import fhnw.ws6c.theapp.model.AuthModel
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.model.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(model: PlantModel, authModel: AuthModel) {
    with(model) {
        Box(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 5.dp)
                .wrapContentHeight()
                .shadow(elevation = 2.dp, ambientColor = Color.LightGray)
        ) {
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
                    },
                    actions = { dropDownMenu(authModel) }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBottomAppBar(model: PlantModel) {
    with(model) {
        Box(
            modifier = Modifier
                .padding(0.dp, 15.dp, 0.dp, 0.dp)
                .wrapContentHeight()
                .shadow(elevation = 10.dp, ambientColor = Color.Black)
        )
        {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                Screen.entries.forEach { screen ->
                    if (screen != Screen.PLANT && screen != Screen.SIGNUP && screen != Screen.LOGIN) {
                        var countPlantThirsty = countPlantsThatNeedWater()
                        NavigationBarItem(
                            icon = {
                                if (countPlantThirsty > 0 && screen.icon == Screen.AAAA.icon) {
                                    BadgedBox(badge = {
                                        Badge {
                                            Text(countPlantThirsty.toString(), color = Color.White)
                                        }
                                    }) {
                                        Icon(
                                            screen.icon, screen.title,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                } else Icon(
                                    screen.icon, screen.title,
                                    modifier = Modifier.size(30.dp)
                                )
                            },
                            label = { Text(screen.title) },
                            selected = currentScreen == screen,
                            onClick = { currentScreen = screen },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                indicatorColor = MaterialTheme.colorScheme.background
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlantBox(model: PlantModel, plant: Plant) {
    with(model) {
        Box(
            modifier = Modifier
                .height(175.dp)
                .fillMaxWidth(0.49f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (plant.needsWater.value == true)
                    {
                        MaterialTheme.colorScheme.tertiaryContainer
                    } else if (plant.needsWater.value == false)
                    {
                        MaterialTheme.colorScheme.secondaryContainer
                    }
                    else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
                .clickable {
                    currentPlant = plant
                    currentScreen = Screen.PLANT
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AAAAText(plant)
                PlantImage(plant)
                PlantNameText(plant)
            }
        }
    }
}

@Composable
fun PlantNameText(plant: Plant) {
    with(plant) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun AAAAText(plant: Plant) {
    if (plant.needsWater.value == true) {
        Text(
            text = "AAAAA!!",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
    } else if(plant.needsWater.value == null) {
        Text(
            text = "???",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        Text(
            text = "",
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun PlantImage(plant: Plant) {
    with(plant) {
        if (needsWater.value == true) {
            Image(
                painter = painterResource(getPictureDrawable()),
                contentDescription = "Sad Plant",
                modifier = Modifier
                    .size(100.dp)
            )
        } else {
            Image(
                painter = painterResource(getPictureDrawable()),
                contentDescription = "Happy Plant",
                modifier = Modifier
                    .size(100.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(labelValue: String) {
    val textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        label = { Text(text = labelValue) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
        },
    )
}

@Composable
fun dropDownMenu(model: AuthModel) {
    with(model) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = { }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = { logout() }
                )
            }
        }
    }
}




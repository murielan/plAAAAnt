package fhnw.ws6c.theapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.model.AuthModel
import fhnw.ws6c.theapp.model.PlantModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(model: PlantModel, authModel: AuthModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    with(model) {
        Scaffold(
            topBar = { NavigationTopAppBar(model, authModel) },
            content = { innerPadding -> HomeContent(model, innerPadding) },
            bottomBar = { NavigationBottomAppBar(model) },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)  // Attach SnackbarHostState to Scaffold
            }
        )

        if (model.connectionFailed or model.firebaseError) {
            LaunchedEffect(snackbarHostState) {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = notificationMessage,
                        actionLabel = "Retry"
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            model.connectAndSubscribe()
                            model.resetConnectionFailure()
                        }
                        SnackbarResult.Dismissed -> { }
                    }
                    model.resetConnectionFailure()
                }
            }
        }
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(plantList) { item ->
                    PlantBox(model = model, plant = item)
                }
            }
            /*FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    maxItemsInEachRow = 2
                ) {
                    for (plant in plantList) {
                        PlantBox(model, plant)
                    }
            }*/
        }
    }
}







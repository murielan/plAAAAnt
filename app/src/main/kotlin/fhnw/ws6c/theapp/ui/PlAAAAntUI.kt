package fhnw.ws6c.theapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import fhnw.ws6c.theapp.model.AuthModel
import fhnw.ws6c.theapp.model.PlantModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.ui.screens.AAAAScreen
import fhnw.ws6c.theapp.ui.screens.HomeScreen
import fhnw.ws6c.theapp.ui.screens.PlantScreen
import fhnw.ws6c.theapp.ui.screens.LoginScreen
import fhnw.ws6c.theapp.ui.screens.SignUpScreen
import fhnw.ws6c.theapp.ui.theme.AppTheme

@Composable
fun PlAAAAntUI(authModel: AuthModel, model: PlantModel) {
    with(model) {
        AppTheme {
            Crossfade(targetState = currentScreen, label = "") { screen ->
                when (screen) {
                    Screen.SIGNUP -> { SignUpScreen(authModel) }
                    Screen.LOGIN -> { LoginScreen(authModel) }
                    Screen.HOME -> { HomeScreen(model, authModel) }
                    Screen.PLANT -> { PlantScreen(model, authModel)}
                    Screen.AAAA -> { AAAAScreen(model, authModel)}
                }
            }
        }
    }
}
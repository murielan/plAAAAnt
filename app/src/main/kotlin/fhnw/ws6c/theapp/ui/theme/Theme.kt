package fhnw.ws6c.theapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val appLightColors = lightColorScheme(
    /*primary = replyLightPrimary,
    onPrimary = replyLightOnPrimary,
    primaryContainer = replyLightPrimaryContainer,
    onPrimaryContainer = replyLightOnPrimaryContainer,
    inversePrimary = replyLightPrimaryInverse,
    secondary = replyLightSecondary,
    onSecondary = replyLightOnSecondary,
    secondaryContainer = replyLightSecondaryContainer,
    onSecondaryContainer = replyLightOnSecondaryContainer,
    tertiary = replyLightTertiary,
    onTertiary = replyLightOnTertiary,
    tertiaryContainer = replyLightTertiaryContainer,
    onTertiaryContainer = replyLightOnTertiaryContainer,
    error = replyLightError,
    onError = replyLightOnError,
    errorContainer = replyLightErrorContainer,
    onErrorContainer = replyLightOnErrorContainer,
    background = replyLightBackground,
    onBackground = replyLightOnBackground,
    surface = replyLightSurface,
    onSurface = replyLightOnSurface,
    inverseSurface = replyLightInverseSurface,
    inverseOnSurface = replyLightInverseOnSurface,
    surfaceVariant = replyLightSurfaceVariant,
    onSurfaceVariant = replyLightOnSurfaceVariant,
    outline = replyLightOutline*/

    //Background colors
    primary          = blue,
    primaryContainer = blueLight,
    secondary        = green,
    secondaryContainer = greenLight,
    tertiary        = orange,
    tertiaryContainer = orangeLight,

    background       = Color.White,
    surface          = Color.White,
    error            = error,

    //Typography and icon colors
    onPrimary        = Color.White,
    onSecondary      = Color.White,
    onBackground     = TextColor,
    onSurface        = TextColor,
    onError          = TextColor,

)

@Composable
fun AppTheme(content: @Composable() () -> Unit) {

    MaterialTheme(
            colorScheme = appLightColors,
            shapes      = shapes,
            content     = content
    )
}
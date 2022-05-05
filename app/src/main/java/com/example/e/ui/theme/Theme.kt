package com.example.e.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = DeepBlueL,
    primaryVariant = EasternBlueL,
    secondary = FlushOrangeL,
    secondaryVariant = SelectiveYellowL,
    surface = CornflowerD,
    background = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorPalette = lightColors(
    primary = FocusBlue,
    primaryVariant = FocusBlue900,
    secondary = FlushOrange,
    secondaryVariant = SelectiveYellow,
    surface = Color.White,
    background = LightGray,
    onPrimary = Color.White,
    onSecondary = DeepFocusBlue,
    onBackground = DeepFocusBlue,
    onSurface = DeepFocusBlue,
)

@get:Composable
val Colors.debt: Color
    get() = if (isLight) Red else RedL

@get:Composable
val Colors.loan: Color
    get() = if (isLight) Green else GreenL

@get:Composable
val Colors.secondarySurface: Color
    get() = if (isLight) CornflowerLL else CornflowerDD

@Composable
fun ETheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

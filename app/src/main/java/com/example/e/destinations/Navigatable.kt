package com.example.e.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import java.time.LocalDateTime

interface Navigatable {
    fun getRoute(): String

    @Composable
    fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    )

    fun getArguments(): List<NamedNavArgument> = emptyList()
}

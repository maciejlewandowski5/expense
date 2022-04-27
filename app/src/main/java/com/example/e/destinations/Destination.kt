package com.example.e.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalSerializationApi
enum class Destination(
    private val navigatable: Navigatable
) {
    HOME(Home),
    ADD_EXPENSE(AddExpense),
    ADD_GROUP(AddGroup),
    SETTLE(Settle),
    LOGIN(Login);

    val route = navigatable.getRoute()

    @Composable
    fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) = navigatable.Content(
        navBackStackEntry = navBackStackEntry,
        navHostController = navHostController,
        showDateTimePicker = showDateTimePicker,
        message = message
    )
}

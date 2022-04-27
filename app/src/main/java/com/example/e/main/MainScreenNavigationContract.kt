package com.example.e.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.e.destinations.Destination
import kotlinx.serialization.ExperimentalSerializationApi

interface MainScreenNavigationContract {
    fun goToAddExpense(): Unit?
    fun menuAction(): Unit?
    fun goToSettlement(): Unit?
    fun goToAddNewGroup(): Unit?
    fun goToLogin()
}

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalSerializationApi
fun mainScreenNavigationContract(navController: NavHostController) =
    object : MainScreenNavigationContract {
        override fun goToAddExpense() =
            navController.navigate(Destination.ADD_EXPENSE.route)

        override fun menuAction() =
            TODO("Not yet implemented")

        override fun goToSettlement() =
            navController.navigate(Destination.SETTLE.route)

        override fun goToAddNewGroup() =
            navController.navigate(Destination.ADD_GROUP.route)

        override fun goToLogin() =
            navController.navigate(Destination.LOGIN.route)
    }

package com.example.e

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.e.addexpense.AddExpenseViewModel
import com.example.e.addexpense.view.AddExpenseScreen
import com.example.e.addgroup.AddGroupViewModel
import com.example.e.addgroup.view.AddGroupScreen
import com.example.e.main.MainScreenNavigationContract
import com.example.e.main.MainViewModel
import com.example.e.main.MainViewScreen
import com.example.e.settle.SettleScreen
import com.example.e.settle.SettleViewModel
import java.time.LocalDateTime

object Navigation {

    enum class Destinations(val route: String) {
        HOME("home"), ADD_EXPENSE("add_expense"), ADD_GROUP("add_group"), SETTLE("settle")
    }

    @Composable
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    fun NavigationComponent(
        navController: NavHostController,
        showDatePicker: ((LocalDateTime) -> Unit) -> Unit,
        payOffText: String
    ) {
        NavHost(
            navController = navController,
            startDestination = Destinations.HOME.route
        ) {
            composable(Destinations.HOME.route) {
                val viewModel = hiltViewModel<MainViewModel>()
                MainViewScreen(
                    viewModel,
                    mainScreenNavigationContract(navController)
                )
                viewModel.fetchExpenses()
            }
            composable(Destinations.ADD_EXPENSE.route) {
                val viewModel = hiltViewModel<AddExpenseViewModel>()
                AddExpenseScreen(
                    viewModel, { showDatePicker { viewModel.setDate(it) } },
                    { navController.navigate(Destinations.HOME.route) },
                )
                viewModel.loadParticipants()
            }
            composable(Destinations.ADD_GROUP.route) {
                val viewModel = hiltViewModel<AddGroupViewModel>()
                AddGroupScreen(viewModel) { navController.navigate(Destinations.HOME.route) }
                viewModel.fetchUsers()
            }
            composable(Destinations.SETTLE.route) {
                val viewModel = hiltViewModel<SettleViewModel>()
                SettleScreen(viewModel)
                viewModel.init(payOffText)
            }
        }
    }

    @Composable
    private fun mainScreenNavigationContract(navController: NavHostController) =
        object : MainScreenNavigationContract {
            override fun goToAddExpense() =
                navController.navigate(Destinations.ADD_EXPENSE.route)

            override fun menuAction() =
                TODO("Not yet implemented")

            override fun goToSettlement() =
                navController.navigate(Destinations.SETTLE.route)

            override fun goToAddNewGroup() =
                navController.navigate(Destinations.ADD_GROUP.route)
        }
}

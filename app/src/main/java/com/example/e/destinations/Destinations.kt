@file:OptIn(
    ExperimentalSerializationApi::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)

package com.example.e.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.e.addexpense.AddExpenseViewModel
import com.example.e.addexpense.view.AddExpenseScreen
import com.example.e.addgroup.AddGroupViewModel
import com.example.e.addgroup.view.AddGroupScreen
import com.example.e.destinations.Navigation.navigateHome
import com.example.e.login.screen.LoginScreen
import com.example.e.login.screen.LoginViewModel
import com.example.e.main.MainViewModel
import com.example.e.main.MainViewScreen
import com.example.e.main.mainScreenNavigationContract
import com.example.e.settle.SettleScreen
import com.example.e.settle.SettleViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime

object Home : Navigatable {
    override fun getRoute(): String = "home"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val viewModel = hiltViewModel<MainViewModel>()
        MainViewScreen(viewModel, mainScreenNavigationContract(navHostController))
        LaunchedEffect(key1 = Unit, block = { viewModel.fetchExpenses() })
    }
}

object AddExpense : Navigatable {
    override fun getRoute(): String = "add_expense"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val viewModel =
            hiltViewModel<AddExpenseViewModel>()
        AddExpenseScreen(
            viewModel,
            {
                if (showDateTimePicker != null) showDateTimePicker { viewModel.setDate(it) }
            },
            navHostController.navigateHome(),
        )
        LaunchedEffect(key1 = Unit, block = { viewModel.loadParticipants() })
    }
}

object AddGroup : Navigatable {
    override fun getRoute(): String = "add_group"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val viewModel = hiltViewModel<AddGroupViewModel>()
        AddGroupScreen(viewModel, navHostController.navigateHome())
        LaunchedEffect(key1 = Unit, block = { viewModel.fetchUsers() })
    }
}

object Settle : Navigatable {
    override fun getRoute(): String = "settle"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val viewModel = hiltViewModel<SettleViewModel>()
        SettleScreen(viewModel)
        LaunchedEffect(key1 = Unit, block = { if (message != null) viewModel.init(message) })
    }
}

object Login : Navigatable {
    override fun getRoute(): String = "login"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginScreen(viewModel, navHostController.navigateHome())
    }
}

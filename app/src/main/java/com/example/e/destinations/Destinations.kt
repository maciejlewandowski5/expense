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
import androidx.navigation.*
import com.example.e.addexpense.AddExpenseViewModel
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.addexpense.view.AddExpenseScreen
import com.example.e.addgroup.AddGroupViewModel
import com.example.e.addgroup.view.AddGroupScreen
import com.example.e.amount.AmountScreen
import com.example.e.amount.AmountViewModel
import com.example.e.destinations.Navigation.navigateHome
import com.example.e.destinations.Split.NEW_EXPENSE_KEY
import com.example.e.login.screen.LoginScreen
import com.example.e.login.screen.LoginViewModel
import com.example.e.main.MainViewModel
import com.example.e.main.MainViewScreen
import com.example.e.main.mainScreenNavigationContract
import com.example.e.settle.SettleScreen
import com.example.e.settle.SettleViewModel
import com.example.e.split.SplitScreen
import com.example.e.split.SplitViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

object AddExpense : Navigatable {
    override fun getRoute(): String = "add_expense"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val viewModel = hiltViewModel<AddExpenseViewModel>()

        AddExpenseScreen(
            viewModel,
            {
                if (showDateTimePicker != null) showDateTimePicker { viewModel.setDate(it) }
            },
            {
                navHostController.navigate(
                    Destination.AMOUNT.route.replace(
                        "{$NEW_EXPENSE_KEY}",
                        Json.encodeToString(it)
                    )
                )
            }
        )
        LaunchedEffect(key1 = Unit, block = { viewModel.loadParticipants() })
    }
}

object Split : Navigatable {
    override fun getRoute(): String = "split/{$NEW_EXPENSE_KEY}"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val expenseInput = navBackStackEntry.arguments?.getString(NEW_EXPENSE_KEY)?.let {
            Json.decodeFromString<NewExpenseInput>(it)
        }
        val viewModel = hiltViewModel<SplitViewModel>()
        viewModel.newExpense = expenseInput

        SplitScreen(viewModel, navHostController.navigateHome())
    }

    override fun getArguments(): List<NamedNavArgument> =
        listOf(navArgument(NEW_EXPENSE_KEY) { type = NavType.StringType })

    const val NEW_EXPENSE_KEY = "expense_input"
}

object Amount : Navigatable {
    override fun getRoute(): String = "amount/{$NEW_EXPENSE_KEY}"

    @Composable
    override fun Content(
        navBackStackEntry: NavBackStackEntry,
        navHostController: NavHostController,
        showDateTimePicker: (((LocalDateTime) -> Unit) -> Unit)?,
        message: String?
    ) {
        val expenseInput = navBackStackEntry.arguments?.getString(NEW_EXPENSE_KEY)?.let {
            Json.decodeFromString<NewExpenseInput>(it)
        }
        val viewModel = hiltViewModel<AmountViewModel>()
        viewModel.expenseInput = expenseInput

        AmountScreen(
            viewModel, {
                navHostController.navigate(
                    Destination.SPLIT.route.replace(
                        "{$NEW_EXPENSE_KEY}",
                        Json.encodeToString(it)
                    )
                )
            },
            navHostController.navigateHome()
        )
    }

    override fun getArguments(): List<NamedNavArgument> =
        listOf(navArgument(NEW_EXPENSE_KEY) { type = NavType.StringType })
}

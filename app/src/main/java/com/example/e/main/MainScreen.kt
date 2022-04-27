package com.example.e.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.domain.AccountingGroup
import com.example.e.expense.ExpensesList
import com.example.e.expense.ExpensesState
import com.example.e.main.spendings.CurrentSpendingState
import com.example.e.main.spendings.HeaderCard
import com.example.e.sampleConversation
import com.example.e.sampleExpenses
import com.example.e.source.SwitchSourceCardContract
import com.example.e.ui.theme.ETheme
import kotlinx.serialization.ExperimentalSerializationApi

@Composable
@ExperimentalSerializationApi
fun MainViewScreen(
    mainViewModel: MainViewModel,
    navigation: MainScreenNavigationContract
) {
    val currentSpendingState by mainViewModel.currentSpending.observeAsState(
        CurrentSpendingState.Loading
    )
    val expensesState by mainViewModel.expensesState.observeAsState(ExpensesState.Loading)
    val isSourceRemote by mainViewModel.isSourceRemote.observeAsState(false)
    MainViewContent(
        currentSpendingState = currentSpendingState,
        expenses = expensesState,
        navigation = navigation,
        contract = mainViewModel,
        switchSourceContract = object : SwitchSourceCardContract {
            override fun isSourceRemote() = isSourceRemote

            override fun switchSource(isSourceRemote: Boolean) {
                mainViewModel.switchSource(isSourceRemote)
            }

            override fun onSourceSwitchedToTrue() {
                navigation.goToLogin()
            }

            override fun onSourceSwitchedToFalse() {}
        }
    )
}

@Composable
fun MainViewContent(
    currentSpendingState: CurrentSpendingState,
    expenses: ExpensesState,
    navigation: MainScreenNavigationContract,
    contract: MainViewContract,
    switchSourceContract: SwitchSourceCardContract
) {
    Scaffold(
        floatingActionButton = {
            if (currentSpendingState !is CurrentSpendingState.Success || currentSpendingState.headerCardData.groupCardData.groups.isNotEmpty()) {
                FloatingActionButton(
                    onClick = navigation::goToAddExpense,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.addNewExpense)
                    )
                }
            }
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                HeaderCard(
                    currentSpendingState = currentSpendingState,
                    navigation = navigation,
                    contract = contract,
                    switchSourceContract = switchSourceContract
                )
                Spacer(modifier = Modifier.height(4.dp))
                Divider()
                Spacer(modifier = Modifier.height(4.dp))
                ExpensesList.ExpensesCard(expenses = expenses)
            }
        },
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Default Dark Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Default Light Mode"
)
@Composable
fun DefaultPreview() {
    ETheme {
        MainViewContent(
            CurrentSpendingState.Success(sampleConversation),
            ExpensesState.Success(sampleExpenses),
            object : MainScreenNavigationContract {
                override fun goToAddExpense() {}
                override fun menuAction() {}
                override fun goToSettlement() {}
                override fun goToAddNewGroup() {}
                override fun goToLogin() {}
            },
            object : MainViewContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun setCurrentGroup(accountingGroup: AccountingGroup) {}
            },
            object : SwitchSourceCardContract {
                override fun isSourceRemote() = true
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun onSourceSwitchedToTrue() {}
                override fun onSourceSwitchedToFalse() {}
            }
        )
    }
}

package com.example.e.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.domain.AccountingGroup
import com.example.e.domain.Expense
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
    val isRefreshingExpenses by mainViewModel.isRefreshingExpenses.observeAsState(false)
    val deleteTimeout by mainViewModel.deleteTimeout.observeAsState(0)
    MainViewContent(
        currentSpendingState = currentSpendingState,
        expenses = expensesState,
        navigation = navigation,
        contract = mainViewModel,
        switchSourceContract = object : SwitchSourceCardContract {
            override fun switchSource(isSourceRemote: Boolean) {
                mainViewModel.switchSource(isSourceRemote)
            }

            override fun onSourceSwitchedToTrue() {
                navigation.goToLogin()
            }

            override fun onSourceSwitchedToFalse() {}
        },
        isSourceRemote = isSourceRemote,
        isRefreshingExpenses = isRefreshingExpenses,
        deleteTimeout = deleteTimeout
    )
}

@Composable
fun MainViewContent(
    currentSpendingState: CurrentSpendingState,
    expenses: ExpensesState,
    navigation: MainScreenNavigationContract,
    contract: MainViewContract,
    switchSourceContract: SwitchSourceCardContract,
    isSourceRemote: Boolean,
    isRefreshingExpenses: Boolean,
    deleteTimeout: Int,
) {
    Scaffold(
        floatingActionButton = {
            if (currentSpendingState !is CurrentSpendingState.Success ||
                currentSpendingState.headerCardData.groupCardData.groups.isNotEmpty()
            ) {
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
        content = { a ->
            Box(contentAlignment = Alignment.BottomCenter) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HeaderCard(
                        currentSpendingState = currentSpendingState,
                        navigation = navigation,
                        contract = contract,
                        switchSourceContract = switchSourceContract,
                        isSourceRemote = isSourceRemote,
                        isRefreshing = isRefreshingExpenses
                    )
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.expenses),
                            color = MaterialTheme.colors.onBackground
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ExpensesList.ExpensesCard(
                            expenses = expenses,
                            isRefreshing = isRefreshingExpenses,
                            onRefresh = contract::onRefresh,
                            deleteExpense = contract::deleteExpense
                        )
                    }
                }

                if (deleteTimeout > 0) {
                    val shape = RoundedCornerShape(8.dp)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .offset(y = (-80).dp)
                            .padding(8.dp)
                            .shadow(shape = shape, elevation = 16.dp)
                            .clip(shape)
                            .background(color = MaterialTheme.colors.surface)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Do you want undo last action?",
                                modifier = Modifier.padding(8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { contract.cancelDelete() }
                                    .padding(4.dp)
                            ) {
                                Text(text = "UNDO", style = MaterialTheme.typography.button)
                            }
                        }
                        LinearProgressIndicator(
                            progress = deleteTimeout / 500f,
                            Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
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
                override fun onRefresh() {}
                override fun deleteExpense(expense: Expense) {}
                override fun cancelDelete() {}
            },
            object : SwitchSourceCardContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun onSourceSwitchedToTrue() {}
                override fun onSourceSwitchedToFalse() {}
            },
            true,
            true,
            300
        )
    }
}

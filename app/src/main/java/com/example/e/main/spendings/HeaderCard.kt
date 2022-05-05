package com.example.e.main.spendings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.*
import com.example.e.domain.AccountingGroup
import com.example.e.domain.Expense
import com.example.e.main.MainScreenNavigationContract
import com.example.e.main.MainViewContract
import com.example.e.source.SwitchSourceCard
import com.example.e.source.SwitchSourceCardContract
import com.example.e.ui.theme.ETheme

@Composable
fun HeaderCard(
    currentSpendingState: CurrentSpendingState,
    navigation: MainScreenNavigationContract,
    contract: MainViewContract,
    switchSourceContract: SwitchSourceCardContract,
    isSourceRemote: Boolean,
    isRefreshing: Boolean
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        SwitchSourceCard(isSourceRemote, switchSourceContract)
        when (currentSpendingState) {
            is CurrentSpendingState.Success -> {
                HeaderCardContent(
                    headerCardData = currentSpendingState.headerCardData,
                    navigation = navigation,
                    contract = contract,
                    isRefreshing = isRefreshing
                )
            }
            is CurrentSpendingState.Loading -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    DotsPulsing()
                }
            }
            is CurrentSpendingState.Error -> ErrorHeader()
        }
    }
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
        HeaderCard(
            CurrentSpendingState.Success(sampleConversation),
            mainScreenNavigationContract(),
            object : MainViewContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun setCurrentGroup(accountingGroup: AccountingGroup) {}
                override fun onRefresh() {}
                override fun deleteExpense(expense: Expense): Unit? {
                    TODO("Not yet implemented")
                }

                override fun cancelDelete(): Unit? {
                    TODO("Not yet implemented")
                }
            },
            object : SwitchSourceCardContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun onSourceSwitchedToTrue() {}
                override fun onSourceSwitchedToFalse() {}
            },
            isSourceRemote = true,
            true
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Loading Dark Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Loading Light Mode"
)
@Composable
fun LoadingPreview() {
    ETheme {
        HeaderCard(
            CurrentSpendingState.Loading,
            mainScreenNavigationContract(),
            object : MainViewContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun setCurrentGroup(accountingGroup: AccountingGroup) {}
                override fun onRefresh() {}
                override fun deleteExpense(expense: Expense): Unit? {
                    TODO("Not yet implemented")
                }

                override fun cancelDelete(): Unit? {
                    TODO("Not yet implemented")
                }
            },
            object : SwitchSourceCardContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun onSourceSwitchedToTrue() {}
                override fun onSourceSwitchedToFalse() {}
            },
            isSourceRemote = true, true
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Error Dark Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Error Light Mode"
)
@Composable
fun ErrorPreview() {
    ETheme {
        HeaderCard(
            CurrentSpendingState.Error,
            mainScreenNavigationContract(),
            object : MainViewContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun setCurrentGroup(accountingGroup: AccountingGroup) {}
                override fun onRefresh() {}
                override fun deleteExpense(expense: Expense): Unit? {
                    TODO("Not yet implemented")
                }

                override fun cancelDelete(): Unit? {
                    TODO("Not yet implemented")
                }
            },
            object : SwitchSourceCardContract {
                override fun switchSource(isSourceRemote: Boolean) {}
                override fun onSourceSwitchedToTrue() {}
                override fun onSourceSwitchedToFalse() {}
            },
            isSourceRemote = true, true
        )
    }
}

@Composable
private fun mainScreenNavigationContract() = object : MainScreenNavigationContract {
    override fun goToAddExpense() {}
    override fun menuAction() {}
    override fun goToSettlement() {}
    override fun goToAddNewGroup() {}
    override fun goToLogin() {}
}

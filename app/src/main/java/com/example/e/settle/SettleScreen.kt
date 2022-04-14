package com.example.e.settle

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.InputWrapperCard
import com.example.e.R
import com.example.e.domain.Expense
import com.example.e.expense.ExpenseCard
import com.example.e.sampleExpenses
import com.example.e.ui.theme.ETheme
import com.example.e.ui.theme.secondarySurface

@Composable
fun SettleScreen(viewModel: SettleViewModel) {
    val expenseState by viewModel.settlementExpenses.observeAsState(SettlementExpenseState.Loading)
    val settlePolicySwitch by viewModel.isSettlePolicyASummary.observeAsState(false)
    val (errorMessage, loadingBar, expenses) = when (expenseState) {
        is SettlementExpenseState.Error -> Triple(
            stringResource(id = R.string.genericError), false,
            emptyList()
        )
        is SettlementExpenseState.Loading -> Triple(null, true, emptyList())
        is SettlementExpenseState.Success -> Triple(
            null, false,
            (expenseState as SettlementExpenseState.Success).expenses
        )
    }
    val setOffMessage = stringResource(
        id = R.string.payyOff
    )
    SettleContent(errorMessage, loadingBar, expenses, {
        viewModel.setPayOffPolicy(
            it,
            setOffMessage
        )
    }, settlePolicySwitch, viewModel::addExpense)
}

@Composable
fun SettleContent(
    errorMessage: String?,
    loadingBar: Boolean,
    expenses: List<Expense>,
    setPayOffPolicy: (Boolean) -> Unit,
    settlePolicySwitch: Boolean,
    addExpense: (Expense) -> Unit
) {
    InputWrapperCard(errorMessage = errorMessage, loadingBar = loadingBar) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(id = R.string.groupPersonalSettelment),
                modifier = Modifier.fillMaxWidth(0.8f),
                color = MaterialTheme.colors.onBackground
            )
            Switch(checked = settlePolicySwitch, onCheckedChange = setPayOffPolicy)
        }
        LazyColumn() {
            itemsIndexed(expenses) { index, item ->
                ExpenseSettleCard(
                    item, index % 2 == 0, settlePolicySwitch,
                    addExpense
                )
            }
        }
    }
}

@Composable
fun ExpenseSettleCard(
    expense: Expense,
    lighterBackground: Boolean,
    isOneSettlement: Boolean,
    addExpense: (Expense) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (lighterBackground) MaterialTheme.colors.background else MaterialTheme.colors.secondarySurface)
    ) {
        Box(Modifier.fillMaxWidth(0.9f)) {
            ExpenseCard.ExpenseCard(
                expense = expense,
                lighterBackground = lighterBackground,
                isOneSettlement
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colors.background)
                .border(color = MaterialTheme.colors.primary, width = 1.dp, shape = CircleShape)
                .clickable { addExpense(expense) }
        ) {
            Icon(
                Icons.Filled.Done,
                contentDescription = stringResource(id = R.string.pay),
                tint = MaterialTheme.colors.onBackground
            )
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
fun SettleContentPreview() {
    ETheme {
        SettleContent("Example error message,", false, sampleExpenses, {}, false, {})
    }
}

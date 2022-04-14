package com.example.e.expense

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.e.DotsPulsing
import com.example.e.R
import com.example.e.sampleExpenses
import com.example.e.ui.theme.ETheme

object ExpensesList {
    @Composable
    fun ExpensesCard(expenses: ExpensesState) {
        when (expenses) {
            is ExpensesState.Success -> {
                LazyColumn {
                    itemsIndexed(items = expenses.expenses) { index, item ->
                        Column {
                            ExpenseCard.ExpenseCard(item, index % 2 == 0)
                            Divider()
                        }
                    }
                }
            }
            ExpensesState.Error -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = stringResource(id = R.string.tryAgainLaterError))
            }
            ExpensesState.Loading -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                DotsPulsing()
            }
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
fun ExpensesPreview() {
    ETheme {
        ExpensesList.ExpensesCard(
            ExpensesState.Success(sampleExpenses)
        )
    }
}